package util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class TestUtils {

	/**
	 * Copia el archivo desde resources a una carpeta temporal y lo devuelve como File
	 * @param filename
	 * @return
	 */
	public static File getFileFromResources(String filename) {
		var resource = TestUtils.class.getClassLoader().getResource(filename);
		if( resource != null) {
			try {				
				var tmpDirPath = Files.createTempDirectory(null);
				var source = Paths.get(resource.toURI());
				var fArr = filename.split("/");
				var f = fArr[ fArr.length-1 ];
				var target = tmpDirPath.resolve(f);
				Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
				return target.toFile();
			} catch (URISyntaxException | IOException e) {
				e.printStackTrace();
			}
		}
		throw new RuntimeException("Archivo "+filename+" NO encontrado.");
	}

	
}
