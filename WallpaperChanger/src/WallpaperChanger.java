import com.sun.jna.Native;

import java.io.File;

import com.sun.jna.Library;
import com.sun.jna.win32.W32APIOptions;

/**
 * Utility class that changes the wallpaper of a WINDOWS desktop.
 */
public class WallpaperChanger {
	private static final int SPI_SETDESKWALLPAPER = 0x0014;
	private static final int SPIF_UPDATEINFILE = 0x01;
	private static final int SPIF_SENDCHANGE = 0x02;
	
	private WallpaperChanger() {
		
	}
	
	//credits for the interface go to shakemart: https://github.com/shakemart/WallpaperChanger/tree/master
	public interface User32 extends Library {
		User32 INSTANCE = (User32) Native.load("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);		
		boolean SystemParametersInfo (int uiAction, int uiParam, String pvParam, int fWinIni);
	}
	
	/**
	 * Method that changes the wallpaper of a WINDOWS desktop given a specified path
	 * @param path the path the location of the wallpaper on the system
	 */
	//credits for method go to vlogommentary: https://www.youtube.com/watch?v=WdRovYYioz0&ab_channel=vlogommentary
	public static void ChangeWallpaper(String name) {
		File temp = new File(String.format("src/Wallpapers/%s.jpg", name));
		String path = temp.getAbsolutePath();
		boolean result = User32.INSTANCE.SystemParametersInfo(SPI_SETDESKWALLPAPER, 0, path, SPIF_UPDATEINFILE|SPIF_SENDCHANGE);
		if(result) System.out.println("Successful");
		else System.out.println("Failure");
	}
	
	public static void main(String[] args) {
		WallpaperChanger.ChangeWallpaper("Roblox");
	}
}
