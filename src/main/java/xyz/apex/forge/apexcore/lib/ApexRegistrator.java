package xyz.apex.forge.apexcore.lib;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;

public class ApexRegistrator<REGISTRATOR extends ApexRegistrator<REGISTRATOR>> extends AbstractRegistrator<REGISTRATOR>
{
	protected ApexRegistrator(String modId)
	{
		super(modId);
	}
}
