package net.minecraft.src.CraftGuide;

import net.minecraft.item.ItemStack;
import net.minecraft.src.CraftGuide.API.IRecipeTemplateResizable;
import uristqwerty.CraftGuide.DefaultRecipeTemplate;
import uristqwerty.CraftGuide.api.ExtraSlot;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.SlotType;
import uristqwerty.gui.texture.Texture;

@Deprecated
public class FakeRecipeTemplate implements IRecipeTemplateResizable
{
	private DefaultRecipeTemplate actualTemplate;
	private int[] slotMapping;

	public FakeRecipeTemplate(net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot[] slots, ItemStack craftingType,
			Texture background, Texture backgroundSelected)
	{
		actualTemplate = new DefaultRecipeTemplate(convertItemSlots(slots), craftingType, background, backgroundSelected);
	}

	private ItemSlot[] convertItemSlots(
			net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot[] oldSlots)
	{
		ItemSlot[] slots = new ItemSlot[oldSlots.length];
		boolean needMapping = false;

		for(int i = 0; i < slots.length; i++)
		{
			if(oldSlots[i].index != i)
			{
				needMapping = true;
			}

			slots[i] = convertSlot(oldSlots[i]);
		}

		if(needMapping)
		{
			slotMapping = new int[oldSlots.length];

			for(int i = 0; i < oldSlots.length; i++)
			{
				slotMapping[i] = oldSlots[i].index;
			}
		}

		return slots;
	}

	private ItemSlot convertSlot(
			net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot slot)
	{
		if(slot instanceof net.minecraft.src.CraftGuide.API.ExtraSlot2)
		{
			return new ExtraSlot(slot.x, slot.y, slot.width, slot.height,
					((net.minecraft.src.CraftGuide.API.ExtraSlot2)slot).displayed)
				.clickable(((net.minecraft.src.CraftGuide.API.ExtraSlot2)slot).canClick)
				.showName(((net.minecraft.src.CraftGuide.API.ExtraSlot2)slot).showName)
				.setSlotType(((net.minecraft.src.CraftGuide.API.ExtraSlot2)slot).canFilter?
						SlotType.OTHER_SLOT : SlotType.DISPLAY_SLOT);
		}
		else if(slot instanceof net.minecraft.src.CraftGuide.API.ExtraSlot)
		{
			return new ExtraSlot(slot.x, slot.y, slot.width, slot.height,
					((net.minecraft.src.CraftGuide.API.ExtraSlot)slot).displayed)
					.setSlotType(SlotType.DISPLAY_SLOT);
		}
		else if(slot instanceof net.minecraft.src.CraftGuide.API.OutputSlot)
		{
			return new ItemSlot(slot.x, slot.y, slot.width, slot.height, slot.drawQuantity)
							.setSlotType(SlotType.OUTPUT_SLOT);
		}
		else
		{
			return new ItemSlot(slot.x, slot.y, slot.width, slot.height, slot.drawQuantity);
		}
	}

	public uristqwerty.CraftGuide.api.RecipeTemplate getRealTemplate()
	{
		return actualTemplate;
	}

	@Override
	public IRecipeTemplateResizable setSize(int width, int height)
	{
		actualTemplate.setSize(width, height);
		return this;
	}

	public Object[] convertItemsList(ItemStack[] items)
	{
		if(slotMapping == null)
		{
			return items;
		}

		Object[] result = new Object[slotMapping.length];

		for(int i = 0; i < slotMapping.length; i++)
		{
			if(slotMapping[i] != -1)
			{
				result[i] = items[slotMapping[i]];
			}
		}

		return result;
	}
}
