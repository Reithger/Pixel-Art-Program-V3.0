package control.config;

import java.io.File;

import filemeta.config.Config;
import filemeta.config.ValidateFiles;

public class ValidateConfig implements ValidateFiles{

	@Override
	public int validateFile(Config c, File f) {
		// TODO Auto-generated method stub
		return Config.CONFIG_VERIFY_SUCCESS;
	}

}
