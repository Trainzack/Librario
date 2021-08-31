package eli.projects.spprototype;



import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.*;

/**
 * This class contains all of the settings that get read from and saved in settings.yml.
 */
public class Settings {

	static ObjectMapper yamlObjectMapper;

	static String SETTINGS_PATH = "settings.yml";
	
	@JsonProperty("library-path")
	private String libraryLocation;

	public Settings() {
		super();
	}
	
	/**
	 * Create a new settings object from the settings file.
	 * If the settings file doesn't exist, create a Settings object and save it to the settings file.
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static Settings loadSettings() throws JsonParseException, JsonMappingException, IOException {
		initObjectMapper();
	
		File settingsPath = new File(SETTINGS_PATH);
		if (settingsPath.exists()) {
			return yamlObjectMapper.readValue(new File(SETTINGS_PATH), Settings.class);
		} else {
			Settings settings = new Settings();
			settings.saveSettings();
			return settings;
		}
		
	}
	
	/**
	 * Saves this settings object to the settings file.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public void saveSettings() throws JsonGenerationException, JsonMappingException, IOException {
		initObjectMapper();
		
		yamlObjectMapper.writeValue(new File(SETTINGS_PATH), this);
	}
	
	public String getLibraryLocation() {
		return libraryLocation;
	}

	public void setLibraryLocation(String libraryLocation) {
		this.libraryLocation = libraryLocation;
		try {
			this.saveSettings();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * If we don't already have a YAML object mapper, create one.
	 * This function should be called before we use the value of yamlObjectMapper.
	 */
	private static void initObjectMapper() {
		if (yamlObjectMapper == null) {
			yamlObjectMapper = new ObjectMapper(new YAMLFactory());
		}
	}
	
}
