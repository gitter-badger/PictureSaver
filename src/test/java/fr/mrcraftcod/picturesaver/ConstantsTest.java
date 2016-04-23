package fr.mrcraftcod.picturesaver;

import fr.mrcraftcod.picturesaver.enums.ConfigKey;
import fr.mrcraftcod.picturesaver.enums.Resources;
import fr.mrcraftcod.picturesaver.objects.Configuration;
import fr.mrcraftcod.utils.FileUtils;
import fr.mrcraftcod.utils.resources.ResourcesBase;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;

public class ConstantsTest
{
	@Test
	public void resourcesTest()
	{
		Assert.assertEquals("t1", new ResourcesBase(this.getClass()).getPropertyString(Resources.LANG, "test", "val1"));
	}

	@Test
	public void configurationTest() throws InterruptedException, ClassNotFoundException
	{
		File configFile = new File(FileUtils.getHomeFolder(), "configTest.db");
		Configuration configuration = new Configuration(configFile, false);
		configuration.setValue(ConfigKey.EMAIL_FETCH_MAIL, "Test1").done(rowChanged -> {
			configuration.getStringValue(ConfigKey.EMAIL_FETCH_MAIL, val -> {
				Assert.assertEquals("Test1", val);
				configuration.close();
			}, err -> {
				Assert.fail("Couldn't get setting value");
				configuration.close();
			});
		}).fail(err -> {
			Assert.fail("Error setting value");
			configuration.close();
		});
	}
}