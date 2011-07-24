package net.minecraft.src;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

import java.io.PrintStream;
import java.util.*;
import java.lang.reflect.Method;

public class BlockDispenser extends BlockContainer
{

    protected BlockDispenser(int i)
    {
        super(i, Material.rock);
        random = new Random();
/* <<<                
// === */
        blockIndexInTexture = 45;
        try
        {
            Class.forName("DispenserAPI");
            hasDispenserAPI = true;
            System.out.println("AutoWorkbench/Dispensers+: DispenserAPI found");
        }
        catch(Exception exception)
        {
            System.out.println("AutoWorkbench/Dispensers+: DispenserAPI not found");
        }
        try
        {
            Class.forName("ModLoader");
            hasModLoader = true;
            System.out.println("AutoWorkbench/Dispensers+: ModLoader found");
        }
        catch(Exception exception1)
        {
            System.out.println("AutoWorkbench/Dispensers+: ModLoader not found");
        }
// >>>            
    }

    public int tickRate()
    {
        return 4;
    }

    public int idDropped(int i, Random random)
    {
        return Block.dispenser.blockID;
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
        setDispenserDefaultDirection(world, i, j, k);
    }

    private void setDispenserDefaultDirection(World world, int i, int j, int k)
    {
        if(world.multiplayerWorld)
        {
            return;
        }    
        int l = world.getBlockId(i, j, k - 1);
        int i1 = world.getBlockId(i, j, k + 1);
        int j1 = world.getBlockId(i - 1, j, k);
        int k1 = world.getBlockId(i + 1, j, k);
        byte byte0 = 3;
        if(Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1])
        {
            byte0 = 3;
        }
        if(Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l])
        {
            byte0 = 2;
        }
        if(Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1])
        {
            byte0 = 5;
        }
        if(Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1])
        {
            byte0 = 4;
        }
        world.setBlockMetadataWithNotify(i, j, k, byte0);
    }

    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        if(l == 1)
        {
            return blockIndexInTexture + 17;
        }
        if(l == 0)
        {
            return blockIndexInTexture + 17;
        }
        int i1 = iblockaccess.getBlockMetadata(i, j, k);
        if(l != i1)
        {
            return blockIndexInTexture;
        } else
        {
            return blockIndexInTexture + 1;
        }
    }

    public int getBlockTextureFromSide(int i)
    {
        if(i == 1)
        {
            return blockIndexInTexture + 17;
        }
        if(i == 0)
        {
            return blockIndexInTexture + 17;
        }
        if(i == 3)
        {
            return blockIndexInTexture + 1;
        } else
        {
            return blockIndexInTexture;
        }
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.multiplayerWorld)
        {
            return true;
        } else
        {
/* <<<                
            TileEntityDispenser tileentitydispenser = (TileEntityDispenser)world.getBlockTileEntity(i, j, k);
            entityplayer.displayGUIDispenser(tileentitydispenser);
// === */
        	int dir = getAutoWorkbenchDirection(world, i, j, k);
        	if(dir != 0){
        		// display workbench gui
        		
        		int x = toX(dir, i);
        		int y = toY(dir, j);
        		int z = toZ(dir, k);
        		
		        entityplayer.displayWorkbenchGUI(x, y, z);
        	}else{
        		// display dispenser gui
                TileEntityDispenser tileentitydispenser = (TileEntityDispenser)world.getBlockTileEntity(i, j, k);
                entityplayer.displayGUIDispenser(tileentitydispenser);
        	}
// >>>            

            return true;
        }
    }

    private void dispenseItem(World world, int i, int j, int k, Random random)
    {
        int l = world.getBlockMetadata(i, j, k);
        int i1 = 0;
        int j1 = 0;

        int dir = 0;
        if(l == 3)
        {
            j1 = 1;
            dir = 100;
        } else
        if(l == 2)
        {
            j1 = -1;
            dir = 200;
        } else
        if(l == 5)
        {
            i1 = 1;
            dir = 1;
        } else
        {
            i1 = -1;
            dir = 2;
        }
/* <<<                
// === */
        ChunkCoordinates chunkcoordinates = new ChunkCoordinates(i + i1, j, k + j1);
        int i2 = world.getBlockId(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z);
        int j2 = world.getBlockMetadata(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z);
// >>>            
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser)world.getBlockTileEntity(i, j, k);
/* <<<                
// === */
        if((i2 == 9 || i2 == 11 || i2 == 8 || i2 == 10) && j2 == 0)
        {
            for(int k1 = 0; k1 < tileentitydispenser.getSizeInventory(); k1++)
            {
                if(tileentitydispenser.getStackInSlot(k1) != null && tileentitydispenser.getStackInSlot(k1).itemID == 325)
                {
                    world.setBlockAndMetadataWithNotify(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, 0, 0);
                    world.notifyBlocksOfNeighborChange(i, j, k, 0);
                    tileentitydispenser.decrStackSize(k1, 1);
                    if(i2 == 9 || i2 == 8)
                    {
                        tileentitydispenser.setInventorySlotContents(k1, new ItemStack(Item.bucketWater));
                    } else
                    if(i2 == 11 || i2 == 10)
                    {
                        tileentitydispenser.setInventorySlotContents(k1, new ItemStack(Item.bucketLava));
                    }
				    world.func_28106_e(1001, i, j, k, 0);
                    return;
                }
            }

        }
// >>>            
        
        ItemStack itemstack = tileentitydispenser.getRandomStackFromInventory();
        
        
        int x = toX(dir, i);
        int y = toY(dir, j);
        int z = toZ(dir, k);
        
        if(isChestBlock(world, x, y, z)){
        
 	    	IInventory tileentitychest = getChestEntity(world, x, y, z);
            itemstack.stackSize = addItemToInventory(tileentitychest, itemstack.itemID, itemstack.stackSize, 0);
            
            if(itemstack.stackSize <= 0){
		        world.func_28106_e(1001, i, j, k, 0);
            	return;
            }
        }
        
        double d = (double)i + (double)i1 * 0.59999999999999998D + 0.5D;
        double d1 = (double)j + 0.5D;
        double d2 = (double)k + (double)j1 * 0.59999999999999998D + 0.5D;

        if(itemstack == null)
        {
            world.func_28106_e(1001, i, j, k, 0);
        } else
        {
        
/* <<<                
// === */

            if(hasModLoader) {
            	try{
		        	Class modLoaderClass = Class.forName("ModLoader");
		        	Class parameters[] = {World.class, double.class, double.class, double.class, int.class, int.class, ItemStack.class};
		        	Method method = modLoaderClass.getMethod("DispenseEntity", parameters);
				    Object arguments[] = {world, d, d1, d2, i1, j1, itemstack};
		        	Boolean flag = (Boolean)method.invoke(null, arguments);
		        	if(flag){
						world.func_28106_e(2000, i, j, k, i1 + 1 + (j1 + 1) * 3);
                        return;
		        	}
		        }catch(Exception exception){
		        }
            }
// >>>            
        
            if(itemstack.itemID == Item.arrow.shiftedIndex)
            {
                EntityArrow entityarrow = new EntityArrow(world, d, d1, d2);
                entityarrow.setArrowHeading(i1, 0.10000000149011612D, j1, 1.1F, 6F);
                entityarrow.doesArrowBelongToPlayer = true;
                world.entityJoinedWorld(entityarrow);
                world.func_28106_e(1002, i, j, k, 0);
            } else
            if(itemstack.itemID == Item.egg.shiftedIndex)
            {
                EntityEgg entityegg = new EntityEgg(world, d, d1, d2);
                entityegg.setEggHeading(i1, 0.10000000149011612D, j1, 1.1F, 6F);
                world.entityJoinedWorld(entityegg);
                world.func_28106_e(1002, i, j, k, 0);
            } else
            if(itemstack.itemID == Item.snowball.shiftedIndex)
            {
                EntitySnowball entitysnowball = new EntitySnowball(world, d, d1, d2);
                entitysnowball.setSnowballHeading(i1, 0.10000000149011612D, j1, 1.1F, 6F);
                world.entityJoinedWorld(entitysnowball);
                world.func_28106_e(1002, i, j, k, 0);
            } else
/* <<<                
// === Dispensers+ */
            if(itemstack.itemID == 46 && world.getBlockId(i, j - 1, k) == 42)
            {
                EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, d, d1, d2);
                entitytntprimed.motionX = i1;
                entitytntprimed.motionY = 0.10000000149011611D;
                entitytntprimed.motionZ = j1;
                world.entityJoinedWorld(entitytntprimed);
                world.func_28106_e(1002, i, j, k, 0);
            } else
            if(itemstack.itemID == 46 || itemstack.itemID == 326 || itemstack.itemID == 327)
            {
                boolean flag = false;
                if(i2 == 0 || i2 == 8 || i2 == 9 || i2 == 10 || i2 == 11)
                {
                    byte byte0;
                    if(itemstack.itemID == 46)
                    {
                        byte0 = 46;
                        world.playSoundEffect(i, j, k, "step.grass", 1.0F, 1.2F);
                    } else
                    {
                        byte0 = ((byte)(itemstack.itemID != 326 ? 11 : 9));
                        addItemToDispenser(tileentitydispenser, new ItemStack(Item.bucketEmpty));
						world.func_28106_e(1001, i, j, k, 0);
                    }
                    world.setBlockAndMetadataWithNotify(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, byte0, 0);
                    world.notifyBlocksOfNeighborChange(i, j, k, byte0);
                }else{
				    world.func_28106_e(1001, i, j, k, 0);
                }
            } else
            if(itemstack.itemID == 328 || itemstack.itemID == 342 || itemstack.itemID == 343)
            {
                byte byte1 = 0;
                if(itemstack.itemID == 342)
                {
                    byte1 = 1;
                } else
                if(itemstack.itemID == 343)
                {
                    byte1 = 2;
                }
                if(!world.multiplayerWorld)
                {
                    world.entityJoinedWorld(new EntityMinecart(world, d + (double)i1 * 0.80000000000000004D, d1, d2 + (double)j1 * 0.80000000000000004D, byte1));
                }
		        world.func_28106_e(1001, i, j, k, 0);
            } else
            if(itemstack.itemID == 333)
            {
                if(!world.multiplayerWorld)
                {
                    world.entityJoinedWorld(new EntityBoat(world, d + (double)i1 * 0.80000000000000004D, d1, d2 + (double)j1 * 0.80000000000000004D));
                }
		        world.func_28106_e(1001, i, j, k, 0);
            } else
            {
/* <<<                
                EntityItem entityitem = new EntityItem(world, d, d1 - 0.29999999999999999D, d2, itemstack);
                double d3 = random.nextDouble() * 0.10000000000000001D + 0.20000000000000001D;
                entityitem.motionX = (double)i1 * d3;
                entityitem.motionY = 0.20000000298023224D;
                entityitem.motionZ = (double)j1 * d3;
                entityitem.motionX += random.nextGaussian() * 0.0074999998323619366D * 6D;
                entityitem.motionY += random.nextGaussian() * 0.0074999998323619366D * 6D;
                entityitem.motionZ += random.nextGaussian() * 0.0074999998323619366D * 6D;
                world.entityJoinedWorld(entityitem);
// === */
                throwItem(world, i1, j1, d, d1, d2, itemstack, random);
// >>>            
		        world.func_28106_e(1000, i, j, k, 0);
            }
// >>>            
            world.func_28106_e(2000, i, j, k, i1 + 1 + (j1 + 1) * 3);

            

        }
    }

	private IInventory getChestEntity(World world, int i, int j, int k){
		// 54 = chest, 23 = dispenser
        IInventory obj = (TileEntityChest)world.getBlockTileEntity(i, j, k);
        if(isChestBlock(world, i - 1, j, k))
        {
            obj = new InventoryLargeChest("Large chest", (TileEntityChest)world.getBlockTileEntity(i - 1, j, k), ((IInventory) (obj)));
        }
        if(isChestBlock(world, i + 1, j, k))
        {
            obj = new InventoryLargeChest("Large chest", ((IInventory) (obj)), (TileEntityChest)world.getBlockTileEntity(i + 1, j, k));
        }
        if(isChestBlock(world, i, j, k - 1))
        {
            obj = new InventoryLargeChest("Large chest", (TileEntityChest)world.getBlockTileEntity(i, j, k - 1), ((IInventory) (obj)));
        }
        if(isChestBlock(world, i, j, k + 1))
        {
            obj = new InventoryLargeChest("Large chest", ((IInventory) (obj)), (TileEntityChest)world.getBlockTileEntity(i, j, k + 1));
        }
        return obj;
	}

    
/* <<<                
// === Dispensers+ */

    private void throwItem(World world, int i1, int j1, double d, double d1, 
            double d2, ItemStack itemstack, Random random)
    {
        EntityItem entityitem = new EntityItem(world, d, d1 - 0.29999999999999999D, d2, itemstack);
        double d3 = random.nextDouble() * 0.10000000000000001D + 0.20000000000000001D;
		entityitem.motionX = (double)i1 * d3;
		entityitem.motionY = 0.20000000298023224D;
		entityitem.motionZ = (double)j1 * d3;        
        entityitem.motionX += random.nextGaussian() * 0.0074999998323619366D * 6D;
        entityitem.motionY += random.nextGaussian() * 0.0074999998323619366D * 6D;
        entityitem.motionZ += random.nextGaussian() * 0.0074999998323619366D * 6D;
        world.entityJoinedWorld(entityitem);
    }

// >>>            

    

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if(l > 0 && Block.blocksList[l].canProvidePower())
        {
            boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k);
            if(flag)
            {
/* <<<                
                world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
// === */
            	int dir = getAutoWorkbenchDirection(world, i, j, k);
		        if(dir == 0){
                    world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
                }

// >>>            

            }
        }
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if(world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k))
        {
/* <<<                
            dispenseItem(world, i, j, k, random);
// === */
        	int dir = getAutoWorkbenchDirection(world, i, j, k);
 			if(dir == 0){
 				// if not connected to auto workbench
 				// do as normal do
                dispenseItem(world, i, j, k, random);
            }
// >>>            
        
        }
    }

    protected TileEntity getBlockEntity()
    {
        return new TileEntityDispenser();
    }

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
    {
        int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if(l == 0)
        {
            world.setBlockMetadataWithNotify(i, j, k, 2);
        }
        if(l == 1)
        {
            world.setBlockMetadataWithNotify(i, j, k, 5);
        }
        if(l == 2)
        {
            world.setBlockMetadataWithNotify(i, j, k, 3);
        }
        if(l == 3)
        {
            world.setBlockMetadataWithNotify(i, j, k, 4);
        }
    }
    
    
/* <<<                
// === Dispensers+ */
    public void onBlockRemoval(World world, int i, int j, int k)
    {
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser)world.getBlockTileEntity(i, j, k);
label0:
        for(int l = 0; l < tileentitydispenser.getSizeInventory(); l++)
        {
            ItemStack itemstack = tileentitydispenser.getStackInSlot(l);
            if(itemstack == null)
            {
                continue;
            }
            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            float f2 = random.nextFloat() * 0.8F + 0.1F;
            do
            {
                if(itemstack.stackSize <= 0)
                {
                    continue label0;
                }
                int i1 = random.nextInt(21) + 10;
                if(i1 > itemstack.stackSize)
                {
                    i1 = itemstack.stackSize;
                }
                itemstack.stackSize -= i1;
                EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                float f3 = 0.05F;
                entityitem.motionX = (float)random.nextGaussian() * f3;
                entityitem.motionY = (float)random.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)random.nextGaussian() * f3;
                world.entityJoinedWorld(entityitem);
            } while(true);
        }

        super.onBlockRemoval(world, i, j, k);
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        float f = 0.0625F;
        return AxisAlignedBB.getBoundingBoxFromPool((float)i + f, j, (float)k + f, (float)(i + 1) - f, (float)(j + 1) - f, (float)(k + 1) - f);
    }
    
// >>>            


/* <<<                
// === Store On Collision */
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        if(getAutoWorkbenchDirection(world, i, j, k) != 0){
            // auto workbench 
            return;
        }
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser)world.getBlockTileEntity(i, j, k);
        if(tileentitydispenser == null) {
            return;
        }
        if((entity instanceof EntityMinecart) && ((EntityMinecart)entity).minecartType == 0)
        {
            if(addItemToDispenser(tileentitydispenser, new ItemStack(Item.minecartEmpty)) != -1)
            {
                world.setEntityDead(entity);
            }
        } else
        if(entity instanceof EntityBoat)
        {
            if(addItemToDispenser(tileentitydispenser, new ItemStack(Item.boat)) != -1)
            {
                world.setEntityDead(entity);
            }
        }

        if(!(entity instanceof EntityItem))
        {
            return;
        }
        ItemStack itemstack = ((EntityItem)entity).item;
        int left = addItemToInventory(tileentitydispenser, itemstack.itemID, itemstack.stackSize, itemstack.getItemDamage());
        if(left > 0){
            itemstack.stackSize = left;
        }else{
            entity.setEntityDead();
        }
    }
// >>>            


/* <<<                
// === */
    
    private int toX(int direction, int location){
    	switch(direction % 10){
    		case 1:
    			return location+1;
    		case 2:
    			return location-1;
    		default:
    			return location;
    	}
    }
    
    private int toY(int direction, int location){
    	return toX(direction / 10, location);
    }
    
    private int toZ(int direction, int location){
    	return toX(direction / 100, location);
    }
	
	
		
	private boolean isChestBlock(World world, int i, int j, int k){
		int blockID = world.getBlockId(i, j, k);
		if(blockID == 54){
			return true;
		}
		
		// 94 = last blockID of original minecraft 
		if(blockID <= 94){
			return false;
		}
		
		// for other mod
		TileEntity tileentity = world.getBlockTileEntity(i, j, k);
		if(tileentity != null){
			if(tileentity instanceof TileEntityChest){
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	private int getAutoWorkbenchDirection(World world, int i, int j, int k){
		// 54 = chest, 23 = dispenser, 58 = workbench
		// isChestBlock - for other chest mod
		
		// straight line
		if(world.getBlockId(i-1, j, k)==58 && isChestBlock(world, i-2, j, k)){
			return 2;
		}else if(world.getBlockId(i+1, j, k)==58 && isChestBlock(world, i+2, j, k)){
			return 1;
		}else if(world.getBlockId(i, j, k-1)==58 && isChestBlock(world, i, j, k-2)){
			return 200;
		}else if(world.getBlockId(i, j, k+1)==58 && isChestBlock(world, i, j, k+2)){
			return 100;
		
		// L shape
		}else if(world.getBlockId(i-1, j, k)==58 && isChestBlock(world, i-1, j, k+1)){
			return 2;
		}else if(world.getBlockId(i-1, j, k)==58 && isChestBlock(world, i-1, j, k-1)){
			return 2;
		}else if(world.getBlockId(i+1, j, k)==58 && isChestBlock(world, i+1, j, k+1)){
			return 1;
		}else if(world.getBlockId(i+1, j, k)==58 && isChestBlock(world, i+1, j, k-1)){
			return 1;
		}else if(world.getBlockId(i, j, k-1)==58 && isChestBlock(world, i+1, j, k-1)){
			return 200;
		}else if(world.getBlockId(i, j, k-1)==58 && isChestBlock(world, i-1, j, k-1)){
			return 200;
		}else if(world.getBlockId(i, j, k+1)==58 && isChestBlock(world, i+1, j, k+1)){
			return 100;
		}else if(world.getBlockId(i, j, k+1)==58 && isChestBlock(world, i-1, j, k+1)){
			return 100;
		
		// vertical
		}else if(world.getBlockId(i, j-1, k)==58 && isChestBlock(world, i, j-2, k)){
			return 20;
		}else if(world.getBlockId(i, j+1, k)==58 && isChestBlock(world, i, j+2, k)){
			return 10;
		}  

		return 0;
	}
	
	
    
    
    private int addItemToInventory(IInventory inv, int itemID, int stackSize, int itemDamage){
    	int sizeInventory = inv.getSizeInventory();
    	
    	if(stackSize > 0){
        	// insert into existed stack if possible
        	for(int i=0; i<sizeInventory; i++){
        		ItemStack itemstack = inv.getStackInSlot(i);
        		if(itemstack == null) 
        		    continue;
    			int maxSize = itemstack.getMaxStackSize();
    			if(itemstack.itemID != itemID)
    			    continue;
    			if(itemstack.stackSize >= maxSize)
    			    continue;
    			    
				int newStackSize = itemstack.stackSize + stackSize;
				if(newStackSize > maxSize)
					newStackSize = maxSize;
				int total = newStackSize - itemstack.stackSize;
				if(total > 0){
					itemstack.stackSize = newStackSize;
					stackSize -= total;
				}
        		if(stackSize <= 0) 
        		    break;
        	}
    	}
    	
    	if(stackSize > 0){
        	// insert into empty stack 
        	for(int i=0; i<sizeInventory; i++){
        		ItemStack itemstack = inv.getStackInSlot(i);
        		if(itemstack != null)
        		    continue;
    			itemstack = new ItemStack(itemID, 0, itemDamage);
    			int maxSize = itemstack.getMaxStackSize();
			    int newStackSize = itemstack.stackSize + stackSize;
				if(newStackSize > maxSize)
					newStackSize = maxSize;
    				
			    int total = newStackSize - itemstack.stackSize;
			    if(total > 0){
				    itemstack.stackSize = newStackSize;
				    stackSize -= total;
				
				    inv.setInventorySlotContents(i, itemstack);
			    }
        		if(stackSize <= 0) 
        		    break;
        	}
    	}
    	
        inv.onInventoryChanged();
        
    	// drop left items
    	return stackSize;
    }
    
    private int addItemToDispenser(TileEntityDispenser tileentitydispenser, ItemStack itemstack)
    {
        for(int i = 0; i < tileentitydispenser.getSizeInventory(); i++)
        {
            if(tileentitydispenser.getStackInSlot(i) == null)
            {
                tileentitydispenser.setInventorySlotContents(i, itemstack);
                return i;
            }
        }

        return -1;
    }
// >>>            


/* <<<                
// === */
    private Random random;
    private static boolean hasModLoader = false;
    private static boolean hasDispenserAPI = false;
// >>>            


}
