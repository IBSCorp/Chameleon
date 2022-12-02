package ru.ibsqa.qualit.recorders;

import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class DefaultVideoRecorderConfiguration implements IVideoRecorderConfiguration {

    @Getter @Setter
    private String extensionPath;

    @Getter @Setter
    private String extensionId;

    @Getter @Setter
    private String[] forSites;

    @Getter @Setter
    private boolean enabled;

    @PostConstruct
    public void preparePlugin(){
        if (enabled && forSites != null && forSites.length > 0){
            try {
                String manifest = extensionPath + "\\manifest.json";
                InputStream is = new FileInputStream(manifest);
                String jsonTxt = IOUtils.toString(is, "UTF-8");
                JSONObject json = new JSONObject(jsonTxt);
                ((JSONObject)json.get("externally_connectable")).put("matches", forSites);
                try (FileWriter file = new FileWriter(manifest)) {
                    file.write(json.toString(3));
                }
            }catch (Exception e){
                log.error(e.getMessage(), e);
                fail("Ошибка при изменение файла manifest плагина RecordPlugin");
            }
        }
    }
}
