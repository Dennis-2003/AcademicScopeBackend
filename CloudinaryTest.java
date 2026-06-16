import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class CloudinaryTest {
    public static void main(String[] args) throws Exception {
        String cloudinaryUrl = "cloudinary://942236826693818:gCYa8U5bUmT5ow_GYVZzjQI4-aU@dd7ke5kte";
        Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
        
        File tempFile = File.createTempFile("test", ".txt");
        FileWriter writer = new FileWriter(tempFile);
        writer.write("Hello World from Antigravity!");
        writer.close();
        
        Map params = ObjectUtils.asMap(
            "resource_type", "raw",
            "folder", "AcademicScope/recursos",
            "public_id", "test_file_antigravity"
        );
        
        Map result = cloudinary.uploader().upload(tempFile, params);
        System.out.println("Upload without preset: " + result.get("secure_url"));

        Map paramsWithPreset = ObjectUtils.asMap(
            "resource_type", "raw",
            "folder", "AcademicScope/recursos",
            "public_id", "test_file_antigravity_preset",
            "upload_preset", "ml_default"
        );
        Map resultPreset = cloudinary.uploader().upload(tempFile, paramsWithPreset);
        System.out.println("Upload with preset: " + resultPreset.get("secure_url"));
    }
}
