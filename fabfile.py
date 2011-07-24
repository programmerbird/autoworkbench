#!/usr/bin/env python
#-*- coding:utf-8 -*-

from fabric.api import *
import os

env.rootpath = os.path.abspath(os.path.dirname(__file__))
env.mcplayeredit = "~/bin/mcplayeredit/mcplayeredit.py"
env.project_name = "AutomaticWorkbench"
env.sourcedir = 'sources'
env.outputdir = 'final_out'
env.test_game = './test_game.sh'
env.reobf = './reobf.sh'

def v1_4():
	env.mcp = "mcp210.zip"
	env.minecraft = "minecraft-1.4.jar"
	env.version = "1.4.1-april05"
	env.workingdir = "1.4"
	env.savegame = "Test1.4"
	env.savefile = '%(rootpath)s/bin/jars/saves/%(savegame)s/level.dat' % env
	env.change_classes = [
		'BlockDispenser',
		'BlockWorkbench',
		'CraftingInventoryWorkbenchCB', 
		'SlotCraftingMultiple',
	]

def v1_4_01():
	env.mcp = "mcp211.zip"
	env.minecraft = "minecraft-1.4_01.jar"
	env.version = "1.4.2-april11"
	env.workingdir = "1.4_01"
	env.savegame = "Test1.4_01"
	env.savefile = '%(rootpath)s/bin/jars/saves/%(savegame)s/level.dat' % env
	env.change_classes = [
		'BlockDispenser',
		'BlockWorkbench',
		'CraftingInventoryWorkbenchCB', 
		'SlotCraftingMultiple',
	]
	
def v1_5_01():
	env.mcp = "mcp212_test1.zip"
	env.minecraft = "minecraft-1.5_01.jar"
	env.version = "1.5.1-april30"
	env.workingdir = "1.5_01"
	env.savegame = "1.5_01"
	env.savefile = '%(rootpath)s/bin/jars/saves/%(savegame)s/level.dat' % env
	env.change_classes = [
		'BlockDispenser',
		'BlockWorkbench',
		'ContainerWorkbench', 
		'SlotCraftingMultiple',
	]
	
	
def v1_6_6():
	env.mcp = "mcp34.zip"
	env.minecraft = "minecraft-1.6.6.jar"
	env.version = "1.6.6-june01"
	env.workingdir = "1.6.6"
	env.savegame = "1.6.6"
	env.savefile = '%(rootpath)s/bin/jars/saves/%(savegame)s/level.dat' % env
	env.change_classes = [
		'BlockDispenser',
		'BlockWorkbench',
		'ContainerWorkbench', 
		'SlotCraftingMultiple',
	]
	env.sourcedir = 'src'
	env.outputdir = 'reobf'
	env.test_game = './startclient.sh'
	env.reobf = './reobfuscate.sh'

	
def v1_7_4():
	env.mcp = "mcp43.zip"
	env.minecraft = "minecraft-1.7.4.jar"
	env.version = "1.7.4-july24"
	env.workingdir = "1.7.4"
	env.savegame = "1.7.4"
	env.savefile = '%(rootpath)s/bin/jars/saves/%(savegame)s/level.dat' % env
	env.change_classes = [
		'BlockDispenser',
		'BlockWorkbench',
		'ContainerWorkbench', 
		'SlotCraftingMultiple',
	]
	env.sourcedir = 'src'
	env.outputdir = 'reobf'
	env.test_game = './startclient.sh'
	env.reobf = './reobfuscate.sh'
	
def head():
	v1_7_4()

def modloader():
	env.minecraft = env.minecraft.replace('.jar', '-modloader.jar')
	
def production():
	env.savefile = 'Autoworkbench'
	
def workspace():
	local('rm -rf bin')
	local('mkdir -p bin')
	with lcd('bin'):
		local('unzip ../resources/%(mcp)s' % env)
		if os.path.exists('%(rootpath)s/bin/scripts' % env):
			local('mv scripts/* .')
		local('cp -rf ~/.minecraft/bin jars/')
		local('cp -rf ~/.minecraft/resources jars/')
		local('cp ../resources/%(minecraft)s jars/bin/minecraft.jar' % env)
		
		local('chmod +x *.sh')
		local('./cleanup.sh')
		local('./decompile.sh')

def recompile():
	local('cp src/%(workingdir)s/minecraft/* bin/%(sourcedir)s/minecraft/net/minecraft/src/' % env)
	with lcd('bin'):
		local('./recompile.sh', capture=False)

def inventory():
	cmds = """
		kit diamond miner
		kit diamond fighter
	"""
	cmds = """
		load %(savefile)s
		clear all
		
		give 64 workbench
		give 64 dispenser
		give 64 chest

		give 3 water bucket
		
		give 64 stone pressure plate
		give 64 log
		give 64 cobblestone
		give 64 coal
		give 64 glass
		give 64 torch
		give 64 redstone
		give 64 string
		give 64 arrow
		give 64 iron ingot
		save
	"""
	cmds = '\n'.join([ line.strip() for line in (cmds % env).strip().split('\n') if line ])
	savefile('/tmp/autoworkbench.inventory', cmds)
	local('cat /tmp/autoworkbench.inventory | %(mcplayeredit)s' % env)
		
def test_game():
	recompile()
	with lcd('bin'):
		local('%(test_game)s' % env, capture=False)
		
def revert():
	for filename in os.listdir('src/%(workingdir)s/minecraft' % env):
		local('cp -f bin/%s/minecraft/net/minecraft/src/%s src/%s/minecraft/%s' % (env.sourcedir, filename, env.workingdir, filename))
	
def reobf():
	local('rm -rf bin/%(outputdir)s/*' % env)
	recompile()
	savefile('bin/conf/client_obfuscation.txt', '\n'.join(env.change_classes))
	with lcd('bin'):
		local('%(reobf)s' % env, capture=False)
		
def package():
	reobf()
	local('rm -rf /tmp/autoworkbenchpack')
	local('rm -rf /tmp/autoworkbenchpack.zip')
	local('mkdir -p /tmp/autoworkbenchpack')
	local('cp src/%(workingdir)s/minecraft/README /tmp/autoworkbenchpack/README.txt' % env)
	local('cp bin/%(outputdir)s/minecraft/* /tmp/autoworkbenchpack/' % env)
	with lcd('/tmp/autoworkbenchpack'):
		local('zip /tmp/autoworkbenchpack.zip * ' % env, capture=False)
	local('mv /tmp/autoworkbenchpack.zip output/%(project_name)s-%(version)s.zip' % env, capture=False)
	
def cleanup():
	local('rm -rf bin')
	
def savefile(filename, content):
	f = open(filename, 'w')
	f.write(content)
	f.close()


