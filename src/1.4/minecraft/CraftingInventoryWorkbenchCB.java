package net.minecraft.src;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 


public class CraftingInventoryWorkbenchCB extends CraftingInventoryCB
{

    public CraftingInventoryWorkbenchCB(InventoryPlayer inventoryplayer, World world, int i, int j, int k)
    {
        craftMatrix = new InventoryCrafting(this, 3, 3);
        craftResult = new InventoryCraftResult();
        field_20133_c = world;
        field_20132_h = i;
        field_20131_i = j;
        field_20130_j = k;
        

/* <<<                
        addSlot(new SlotCrafting(inventoryplayer.player, craftMatrix, craftResult, 0, 124, 35));
// === */
        int dir = getAutoDispenserDirection(world, i, j, k);
        if(dir != 0){
		    addSlot(new SlotCraftingMultiple(inventoryplayer.player, craftMatrix, craftResult, 0, 124, 35));
		}else{
            addSlot(new SlotCrafting(inventoryplayer.player, craftMatrix, craftResult, 0, 124, 35));
		}
// >>>        	
		        
        for(int l = 0; l < 3; l++)
        {
            for(int k1 = 0; k1 < 3; k1++)
            {
                addSlot(new Slot(craftMatrix, k1 + l * 3, 30 + k1 * 18, 17 + l * 18));
            }

        }
        
/* <<<                
// === */
  		if(dir != 0){
  			// found auto dispenser
  			// load !
			int x = toX(dir, i);
			int y = toY(dir, j);
			int z = toZ(dir, k);
	  		TileEntityDispenser tileentitydispenser = (TileEntityDispenser)world.getBlockTileEntity(x, y, z);
  			
  			for(int i1=0; i1<9; i1++){
  				ItemStack itemstack = tileentitydispenser.getStackInSlot(i1);
  				if(itemstack != null){
		  			craftMatrix.setInventorySlotContents(i1, 
		  				new ItemStack(itemstack.itemID, itemstack.stackSize, itemstack.getItemDamage()));
		  		}
  			}	
  		}

// >>>        	


        for(int i1 = 0; i1 < 3; i1++)
        {
            for(int l1 = 0; l1 < 9; l1++)
            {
                addSlot(new Slot(inventoryplayer, l1 + i1 * 9 + 9, 8 + l1 * 18, 84 + i1 * 18));
            }

        }

        for(int j1 = 0; j1 < 9; j1++)
        {
            addSlot(new Slot(inventoryplayer, j1, 8 + j1 * 18, 142));
        }

        onCraftMatrixChanged(craftMatrix);
    }

    public void onCraftMatrixChanged(IInventory iinventory)
    {
/* <<<                
        craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix));
// === */
        World world = field_20133_c;
        int i = field_20132_h;
        int j = field_20131_i;
        int k = field_20130_j;

      	int dir = getAutoDispenserDirection(world, i, j, k);
  		if(dir == 0){
  			// no dispenser found
        
            craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix));
        }else{

  			// dispenser found
		    ItemStack itemstack = CraftingManager.getInstance().findMatchingRecipe(craftMatrix);
		    if(itemstack != null){
	  		
				int limit = 16;
		 
		    	// calculate total items
		    	int total = limit;
		    	for(int i1=0; i1<9; i1++){
		    		ItemStack sourcestack = craftMatrix.getStackInSlot(i1);
		    		if(sourcestack != null && sourcestack.stackSize > 0){
		    			if(total > sourcestack.stackSize){
		    				total = sourcestack.stackSize;
		    			}
		    		}
		    	}
		    	
		    	if (itemstack.getMaxStackSize() < total){
		    		total = itemstack.getMaxStackSize();
		    	}
		    	
		    	itemstack.stackSize *= total;
	    	}
		    craftResult.setInventorySlotContents(0, itemstack);
		    
        }
// >>>            
	}


/* <<<                
// === */
    public ItemStack func_20116_a(int a, int b, EntityPlayer entityplayer) {
  		ItemStack result = super.func_20116_a(a, b, entityplayer);
        World world = field_20133_c;
        int i = field_20132_h;
        int j = field_20131_i;
        int k = field_20130_j;

      	int dir = getAutoDispenserDirection(world, i, j, k);
      	if(dir != 0){
      		onCraftMatrixChanged(null);
      	}
    	return result;
    }
    
    
// >>>    

/* <<<                
// === */

    private int toX(int direction, int location){
    	switch(direction){
    		case 1:
    			return location+1;
    		case -1:
    			return location-1;
    		default:
    			return location;
    	}
    }
    
    private int toY(int direction, int location){
    	switch(direction){
    		case 10:
    			return location+1;
    		case -10:
    			return location-1;
    		default:
    			return location;
    	}
    }
    
    private int toZ(int direction, int location){
    	switch(direction){
    		case 100:
    			return location+1;
    		case -100:
    			return location-1;
    		default:
    			return location;
    	}
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
		if(world.getBlockId(i, j+1, k)==23 && isChestBlock(world, i, j-1, k)){
			return +10;
		}else if(world.getBlockId(i, j-1, k)==23 && isChestBlock(world, i, j+1, k)){
			return -10;
		}else if(world.getBlockId(i+1, j, k)==23 && isChestBlock(world, i-1, j, k)){
			return +1;
		}else if(world.getBlockId(i-1, j, k)==23 && isChestBlock(world, i+1, j, k)){
			return -1;
		}else if(world.getBlockId(i, j, k+1)==23 && isChestBlock(world, i, j, k-1)){
			return +100;
		}else if(world.getBlockId(i, j, k-1)==23 && isChestBlock(world, i, j, k+1)){
			return -100;
		}

		return 0;
	}
// >>>        	

    public void onCraftGuiClosed(EntityPlayer entityplayer)
    {
        super.onCraftGuiClosed(entityplayer);
/* <<<                
        for(int i = 0; i < 9; i++)
        {
            ItemStack itemstack = craftMatrix.getStackInSlot(i);
            if(itemstack != null)
            {
                entityplayer.dropPlayerItem(itemstack);
            }
        }
// === */


        World world = field_20133_c;
        int i = field_20132_h;
        int j = field_20131_i;
        int k = field_20130_j;
  
      	int dir = getAutoDispenserDirection(world, i, j, k);

  		if(dir == 0){
  			// no dispenser found
  			// drop items

            for(int i1 = 0; i1 < 9; i1++)
            {
                ItemStack itemstack = craftMatrix.getStackInSlot(i1);
                if(itemstack != null)
                {
                    entityplayer.dropPlayerItem(itemstack);
                }
            }
  		}else{
  			// store item on dispenser
			int x = toX(dir, i);
			int y = toY(dir, j);
			int z = toZ(dir, k);
	  		TileEntityDispenser tileentitydispenser = (TileEntityDispenser)world.getBlockTileEntity(x, y, z);
  			for(int i1=0; i1<9; i1++){
  				ItemStack itemstack = craftMatrix.getStackInSlot(i1);
  				if(itemstack != null && itemstack.stackSize > 0){
		  			tileentitydispenser.setInventorySlotContents(i1, itemstack);
  				}else{
		  			tileentitydispenser.setInventorySlotContents(i1, null);
  				}
  			}
  		}
// >>>            
        

    }

    public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        if(field_20133_c.getBlockId(field_20132_h, field_20131_i, field_20130_j) != Block.workbench.blockID)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)field_20132_h + 0.5D, (double)field_20131_i + 0.5D, (double)field_20130_j + 0.5D) <= 64D;
    }

    public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    private World field_20133_c;
    private int field_20132_h;
    private int field_20131_i;
    private int field_20130_j;
}



/* <<<                
// === */

class SlotCraftingMultiple extends Slot
{

    public SlotCraftingMultiple(EntityPlayer entityplayer, InventoryCrafting iinventory, IInventory iinventory1, int i, int j, int k)
    {
        super(iinventory1, i, j, k);
        field_25015_e = entityplayer;
        craftMatrix = iinventory;
    }


    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }

    public void onPickupFromSlot(ItemStack itemstack)
    {
    
		int limit = 16;
 
    	// calculate total items
    	int total = limit;
    	for(int i1=0; i1<9; i1++){
            ItemStack sourcestack = craftMatrix.getStackInSlot(i1);
    		if(sourcestack != null && sourcestack.stackSize > 0){
    			if(total > sourcestack.stackSize){
    				total = sourcestack.stackSize;
    			}
    		}
    	}
    	

    	if(itemstack != null){
			if (itemstack.getMaxStackSize() < total){
				total = itemstack.getMaxStackSize();
			}
    	}
    	
    	// achievement
    	/* <<<                
        field_25015_e.func_25058_a(StatList.field_25158_z[itemstack.itemID], 1);
        if(itemstack.itemID == Block.workbench.blockID)
        {
            field_25015_e.func_25058_a(AchievementList.field_25197_d, 1);
        }
    	// === */
        field_25015_e.func_25058_a(StatList.field_25158_z[itemstack.itemID], total);
        if(itemstack.itemID == Block.workbench.blockID)
        {
        	// build workbench
            field_25015_e.func_25058_a(AchievementList.field_25197_d, total);
        }
    	// >>>            
    	
    	
    	// return container
    	/* <<<                

        for(int i = 0; i < craftMatrix.getSizeInventory(); i++)
        {
            ItemStack itemstack1 = craftMatrix.getStackInSlot(i);
            if(itemstack1 == null)
            {
                continue;
            }
            craftMatrix.decrStackSize(i, 1);
            if(itemstack1.getItem().func_21014_i())
            {
                craftMatrix.setInventorySlotContents(i, new ItemStack(itemstack1.getItem().getContainerItem()));
            }
        }
    	// === */
        for(int i = 0; i < craftMatrix.getSizeInventory(); i++)
        {
            ItemStack itemstack1 = craftMatrix.getStackInSlot(i);
            if(itemstack1 == null)
            {
                continue;
            }
            craftMatrix.decrStackSize(i, total);
            if(itemstack1.getItem().func_21014_i())
            {
                craftMatrix.setInventorySlotContents(i, new ItemStack(itemstack1.getItem().getContainerItem()));
            }
        }

    	// >>>            
    	
    	
    }

    public boolean func_25014_f()
    {
        return true;
    }
    
    private final InventoryCrafting craftMatrix;
    private EntityPlayer field_25015_e;
 // >>>        	
    
    
}
