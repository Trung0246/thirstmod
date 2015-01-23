package com.thetorine.thirstmod.core.content.blocks;

import com.thetorine.thirstmod.core.content.ItemLoader;
import com.thetorine.thirstmod.core.content.packs.DrinkLists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDS extends TileEntity implements IInventory {
	public ItemStack[] items = new ItemStack[3];
	public int amountToBuy = 1;
	public int canBuy;
	public int page;

	@Override
	public void updateEntity() {
		if (worldObj != null) {
			if (!DrinkLists.LOADED_DRINKS.isEmpty()) {
				items[0] = DrinkLists.LOADED_DRINKS.get(page).item;
				if (canBuy == 1) {
					if ((items[2] != null) && (items[1] == null)) {
						if (items[2].getUnlocalizedName().equals(ItemLoader.goldCoin.getUnlocalizedName())) {
							if ((DrinkLists.LOADED_DRINKS.get(page).storeRecipe * amountToBuy) <= items[2].stackSize) {
								decrStackSize(2, DrinkLists.LOADED_DRINKS.get(page).storeRecipe * amountToBuy);
								items[1] = new ItemStack(items[0].getItem(), amountToBuy);
								amountToBuy = 1;
								canBuy = 0;
							} else {
								canBuy = 0;
							}
						} else {
							canBuy = 0;
						}
					} else {
						canBuy = 0;
					}
				}
			}
		}
	}

	@Override
	public int getSizeInventory() {
		return items.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return items[var1];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (items[i] != null) {
			if (items[i].stackSize <= j) {
				ItemStack itemstack = items[i];
				items[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = items[i].splitStack(j);
			if (items[i].stackSize == 0) {
				items[i] = null;
			}
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (this.items[i] != null) {
			ItemStack var2 = this.items[i];
			this.items[i] = null;
			return var2;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		items[i] = itemstack;
		if ((itemstack != null) && (itemstack.stackSize > getInventoryStackLimit())) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 0);
		items = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if ((byte0 >= 0) && (byte0 < items.length)) {
				items[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				items[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
	}

	@Override
	public String getInventoryName() {
		return "DrinksShop";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}
}
