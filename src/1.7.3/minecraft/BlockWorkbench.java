package net.minecraft.src;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

/* <<<                
// === */
import java.util.Random;
// >>>        	

public class BlockWorkbench extends Block
{

    protected BlockWorkbench(int i)
    {
        super(i, Material.wood);
        blockIndexInTexture = 59;
    }

    public int getBlockTextureFromSide(int i)
    {
        if(i == 1)
        {
            return blockIndexInTexture - 16;
        }
        if(i == 0)
        {
            return Block.planks.getBlockTextureFromSide(0);
        }
        if(i == 2 || i == 4)
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
            entityplayer.displayWorkbenchGUI(i, j, k);
            return true;
        }
    }
    
    
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
	
	private int getAutoDispenserDirection(World world, int i, int j, int k){
		// 54 = chest, 23 = dispenser
		// isChestBlock - for other chest mod
		
		// straight line
		if(world.getBlockId(i+1, j, k)==23 && isChestBlock(world, i-1, j, k)){
			return 1;
		}else if(world.getBlockId(i-1, j, k)==23 && isChestBlock(world, i+1, j, k)){
			return 2;
		}else if(world.getBlockId(i, j, k+1)==23 && isChestBlock(world, i, j, k-1)){
			return 100;
		}else if(world.getBlockId(i, j, k-1)==23 && isChestBlock(world, i, j, k+1)){
			return 200;
		
		// L shape
		}else if(world.getBlockId(i+1, j, k)==23 && isChestBlock(world, i, j, k+1)){
			return 1;
		}else if(world.getBlockId(i+1, j, k)==23 && isChestBlock(world, i, j, k-1)){
			return 1;
		}else if(world.getBlockId(i-1, j, k)==23 && isChestBlock(world, i, j, k+1)){
			return 2;
		}else if(world.getBlockId(i-1, j, k)==23 && isChestBlock(world, i, j, k-1)){
			return 2;
		}else if(world.getBlockId(i, j, k+1)==23 && isChestBlock(world, i+1, j, k)){
			return 100;
		}else if(world.getBlockId(i, j, k+1)==23 && isChestBlock(world, i-1, j, k)){
			return 100;
		}else if(world.getBlockId(i, j, k-1)==23 && isChestBlock(world, i+1, j, k)){
			return 200;
		}else if(world.getBlockId(i, j, k-1)==23 && isChestBlock(world, i-1, j, k)){
			return 200;
		
		// vertical
		}else if(world.getBlockId(i, j+1, k)==23 && isChestBlock(world, i, j-1, k)){
			return 10;
		}else if(world.getBlockId(i, j-1, k)==23 && isChestBlock(world, i, j+1, k)){
			return 20;
		}  

		return 0;
	}
	
	
	private int getAutoChestDirection(World world, int i, int j, int k){
		// 54 = chest, 23 = dispenser
		// isChestBlock - for other chest mod
		
		// straight line
		if(world.getBlockId(i+1, j, k)==23 && isChestBlock(world, i-1, j, k)){
			return 2;
		}else if(world.getBlockId(i-1, j, k)==23 && isChestBlock(world, i+1, j, k)){
			return 1;
		}else if(world.getBlockId(i, j, k+1)==23 && isChestBlock(world, i, j, k-1)){
			return 200;
		}else if(world.getBlockId(i, j, k-1)==23 && isChestBlock(world, i, j, k+1)){
			return 100;
		
		// L shape
		}else if(world.getBlockId(i+1, j, k)==23 && isChestBlock(world, i, j, k+1)){
			return 100;
		}else if(world.getBlockId(i+1, j, k)==23 && isChestBlock(world, i, j, k-1)){
			return 200;
		}else if(world.getBlockId(i-1, j, k)==23 && isChestBlock(world, i, j, k+1)){
			return 100;
		}else if(world.getBlockId(i-1, j, k)==23 && isChestBlock(world, i, j, k-1)){
			return 200;
		}else if(world.getBlockId(i, j, k+1)==23 && isChestBlock(world, i+1, j, k)){
			return 1;
		}else if(world.getBlockId(i, j, k+1)==23 && isChestBlock(world, i-1, j, k)){
			return 2;
		}else if(world.getBlockId(i, j, k-1)==23 && isChestBlock(world, i+1, j, k)){
			return 1;
		}else if(world.getBlockId(i, j, k-1)==23 && isChestBlock(world, i-1, j, k)){
			return 2;
		
		// vertical
		}else if(world.getBlockId(i, j+1, k)==23 && isChestBlock(world, i, j-1, k)){
			return 20;
		}else if(world.getBlockId(i, j-1, k)==23 && isChestBlock(world, i, j+1, k)){
			return 10;
		}  

		return 0;
	}
	
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
    
    	if(getAutoDispenserDirection(world, i, j, k) == 0){
    		return;
    	}
        if(l > 0 && Block.blocksList[l].canProvidePower())
        {
            boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k);
            if(flag)
            {
                world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
            }
        }


    }


    public void updateTick(World world, int i, int j, int k, Random random)
    {
    	if(getAutoDispenserDirection(world, i, j, k) == 0){
    		return;
    	}
    
        if(world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k))
        {
            autoCraftItem(world, i, j, k, random);
        }
    }
	
	
    public int tickRate()
    {
        return 4;
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

    private void autoCraftItem(World world, int i, int j, int k, Random random)
    {
    	int dir = getAutoDispenserDirection(world, i, j, k);
    	if(dir == 0){
    		return;
    	}
    	int chestDir = getAutoChestDirection(world, i, j, k);
    	int x = toX(dir, i);
    	int y = toY(dir, j);
    	int z = toZ(dir, k);
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser)world.getBlockTileEntity(x, y, z);
        IInventory tileentitychest = getChestEntity(world, toX(chestDir, i), toY(chestDir, j), toZ(chestDir, k));
    	ContainerDispenser dispenserInventory = new ContainerDispenser(new TileEntityChest(), tileentitydispenser);
    	InventoryCrafting inv = new InventoryCrafting(dispenserInventory, 3, 3);
    	
    	for(int i1=0; i1<9; i1++){
    		ItemStack sourcestack = tileentitydispenser.getStackInSlot(i1);
    		inv.setInventorySlotContents(i1, sourcestack);
    	}
    	
    	ItemStack itemstack = CraftingManager.getInstance().findMatchingRecipe(inv);
        if(itemstack != null){
        
			int limit = 16;        	 
        	// calculate total items
        	int total = limit;
        	for(int i1=0; i1<9; i1++){
        		ItemStack sourcestack = tileentitydispenser.getStackInSlot(i1);
        		if(sourcestack != null && sourcestack.stackSize > 0){
        			if(total > sourcestack.stackSize){
        				total = sourcestack.stackSize;
        			}
        		}
        	}
        	
        	if(itemstack.getMaxStackSize() < total){
        		total = itemstack.getMaxStackSize();
        	}
        	
        	// remove resource from chest
        	if(!removeResourceFromChest(tileentitydispenser, tileentitychest, total))
        		return;
    	
	    	// throw itemstack
	    	itemstack.stackSize = total * itemstack.stackSize;

	    	// achievement
			/* <<<                
		    field_25015_e.func_25058_a(StatList.field_25158_z[itemstack.itemID], 1);
		    if(itemstack.itemID == Block.workbench.blockID)
		    {
		        field_25015_e.func_25058_a(AchievementList.field_25197_d, 1);
		    }
			// === */
			EntityPlayer entityplayer = world.getClosestPlayer((double)i+0.5D, (double)j+0.5D, (double)k+0.5D, 16D);
			
			if(entityplayer != null){
				entityplayer.addStat(StatList.field_25158_z[itemstack.itemID], total);
				if(itemstack.itemID == Block.workbench.blockID)
				{
				    entityplayer.addStat(AchievementList.buildWorkBench, total);
				}
			}
			// >>>            
				    	
			throwItem(world, x, y, z, random, itemstack);
        }
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
    
    
    private boolean removeItemFromInventory(IInventory inv, int itemID, int stackSize){
    	int acquired = 0;
    	int sizeInventory = inv.getSizeInventory();
		// -1 instead; we do not want to mess with chest filter system from [store on collision]
    	sizeInventory -= 1;
    	
    	// use unfull stack first
    	for(int i=0; i<sizeInventory; i++){
			ItemStack itemstack = inv.getStackInSlot(i);
			if(itemstack != null 
				&& itemstack.itemID == itemID 
				&& itemstack.getMaxStackSize() != itemstack.stackSize){
				int newStackSize = itemstack.stackSize - stackSize;
				if(newStackSize < 0){
					newStackSize = 0;
				}
				int total = itemstack.stackSize - newStackSize;
				if(total > 0){
					inv.decrStackSize(i, total);
					stackSize -= total;
					acquired += total;
				}
			}
			if(stackSize <= 0){
		        inv.onInventoryChanged();
				return true;
			}
    	}

    	for(int i=0; i<sizeInventory; i++){
			ItemStack itemstack = inv.getStackInSlot(i);
			if(itemstack != null 
				&& itemstack.itemID == itemID){
				int newStackSize = itemstack.stackSize - stackSize;
				if(newStackSize < 0){
					newStackSize = 0;
				}
				int total = itemstack.stackSize - newStackSize;
				if(total > 0){
					inv.decrStackSize(i, total);
					stackSize -= total;
					acquired += total;
				}
			}
			if(stackSize <= 0){
		        inv.onInventoryChanged();
				return true;
			}
    	}
    	
    	// not enough resource
    	// restore and return false
    	addItemToInventory(inv, itemID, acquired, 0);   	
    	return false;
    }
    
    private boolean removeResourceFromChest(IInventory dispenserInv, IInventory chestInv, int limit){
    	int cursor = 0;    	
    	while(cursor < 9){
    	
    		ItemStack sourcestack = dispenserInv.getStackInSlot(cursor);
    		if(sourcestack != null && sourcestack.stackSize > 0){
				int total = sourcestack.stackSize;
				if(total > limit){
					total = limit;
				}
    			if(!removeItemFromInventory(chestInv, sourcestack.itemID, total)){
    				break;
    			}
    		}
    		cursor += 1;
    	}
    	
    	if(cursor == 9){
    		// success !
    		
    		// return container to chest 
			cursor -= 1;
    		while(cursor >= 0){
				ItemStack sourcestack = dispenserInv.getStackInSlot(cursor);
				if(sourcestack != null && sourcestack.stackSize > 0){
				    if(sourcestack.getItem().hasContainerItem()) {
						int total = sourcestack.stackSize;
						if(total > limit){
							total = limit;
						}
				    	addItemToInventory(chestInv, sourcestack.getItem().getContainerItem().shiftedIndex, total, 0);
				    }
				}
    			cursor -= 1;
			}
    		return true;
    	}else{
			cursor -= 1;
    		// restore 
    		while(cursor >= 0){
				ItemStack sourcestack = dispenserInv.getStackInSlot(cursor);
				if(sourcestack != null && sourcestack.stackSize > 0){
					int total = sourcestack.stackSize;
					if(total > limit){
						total = limit;
					}
					addItemToInventory(chestInv, sourcestack.itemID, total, 0);
				}
    			cursor -= 1;
    		}
    		return false;
    	}
    }
    
    private void throwItem(World world, int i, int j, int k, Random random, ItemStack itemstack){
    
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
            EntityItem entityitem = new EntityItem(world, d, d1 - 0.29999999999999999D, d2, itemstack);
            double d3 = random.nextDouble() * 0.10000000000000001D + 0.20000000000000001D;
            entityitem.motionX = (double)i1 * d3;
            entityitem.motionY = 0.20000000298023224D;
            entityitem.motionZ = (double)j1 * d3;
            entityitem.motionX += random.nextGaussian() * 0.0074999998323619366D * 6D;
            entityitem.motionY += random.nextGaussian() * 0.0074999998323619366D * 6D;
            entityitem.motionZ += random.nextGaussian() * 0.0074999998323619366D * 6D;
            world.entityJoinedWorld(entityitem);
            world.func_28106_e(1001, i, j, k, 0);
            world.func_28106_e(2000, i, j, k, i1 + 1 + (j1 + 1) * 3);

        }
        
    }
// >>>        	

}
