#!/usr/bin/env python
#-*- coding:utf-8 -*-

from fabric.api import *
import os

env.rootpath = os.path.abspath(os.path.dirname(__file__))
env.mcplayeredit = "~/bin/mcplayeredit/mcplayeredit.py"
env.project_name = "AutomaticWorkbench"

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
	
def modloader():
	env.minecraft = env.minecraft.replace('.jar', '-modloader.jar')
	
def production():
	env.savefile = 'Autoworkbench'
	
def head():
	v1_4_01()
	
def workspace():
	local('rm -rf bin')
	local('mkdir -p bin')
	with lcd('bin'):
		local('unzip ../resources/%(mcp)s' % env)
		local('mv scripts/* .')
		local('cp -rf ~/.minecraft/bin jars/')
		local('cp -rf ~/.minecraft/resources jars/')
		local('cp ../resources/%(minecraft)s jars/bin/minecraft.jar' % env)
		
		local('chmod +x *.sh')
		local('./cleanup.sh')
		local('./decompile.sh')

def recompile():
	local('cp src/%(workingdir)s/minecraft/* bin/sources/minecraft/net/minecraft/src/' % env)
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
		local('./test_game.sh', capture=False)
		
def revert():
	for filename in os.listdir('src/%(workingdir)s/minecraft' % env):
		local('cp -f bin/sources/minecraft/net/minecraft/src/%s src/%s/minecraft/%s' % (filename, env.workingdir, filename))
	
def reobf():
	local('rm -rf bin/final_out/*')
	recompile()
	savefile('bin/conf/client_obfuscation.txt', '\n'.join(env.change_classes))
	with lcd('bin'):
		local('./reobf.sh', capture=False)
		
def package():
	reobf()
	local('rm -rf /tmp/autoworkbenchpack')
	local('rm -rf /tmp/autoworkbenchpack.zip')
	local('mkdir -p /tmp/autoworkbenchpack')
	local('cp src/%(workingdir)s/minecraft/README /tmp/autoworkbenchpack/README.txt' % env)
	local('cp bin/final_out/minecraft/* /tmp/autoworkbenchpack/')
	with lcd('/tmp/autoworkbenchpack'):
		local('zip /tmp/autoworkbenchpack.zip * ' % env, capture=False)
	local('mv /tmp/autoworkbenchpack.zip output/%(project_name)s-%(version)s.zip' % env, capture=False)
	
def cleanup():
	local('rm -rf bin')
	
def savefile(filename, content):
	f = open(filename, 'w')
	f.write(content)
	f.close()


