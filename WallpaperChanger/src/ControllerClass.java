import java.io.File;

public class ControllerClass {
	public ControllerClass() {
		
	}
	public static void main(String[] args) {
		String wallpaper = FileCopier.GetWallpaperName(ProfileCreator.getActiveWallpaperID());
		File f = new File(String.format("src/Wallpapers/%s.jpg", wallpaper));
		WallpaperChanger.ChangeWallpaper(f.getAbsolutePath());
		System.out.println(f.getAbsolutePath());
	}

}
