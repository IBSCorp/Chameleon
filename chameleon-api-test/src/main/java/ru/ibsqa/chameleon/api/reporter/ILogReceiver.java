package ru.ibsqa.chameleon.api.reporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public interface ILogReceiver {

    void receive(String text);
    void setSkipAttachBody(boolean skipAttachBody);

    default String[] getParts(String text) {
        StringBuffer[] sb = {new StringBuffer(),new StringBuffer()};
        int i = 0;

        try {
            BufferedReader bufReader = new BufferedReader(new StringReader(text));

            String line=null;
            while( (line=bufReader.readLine()) != null ) {
                if ((line.isEmpty() || line.startsWith("Body:") ) && 0==i) {
                    i++;
                } else {
                    if (!sb[i].toString().isEmpty()) sb[i].append("\r\n");
                    sb[i].append(line);
                }
            }
        } catch (IOException e) {
            Logger log = LoggerFactory.getLogger(ILogReceiver.class);
            log.error(e.getMessage(), e);
        }
        String part1 = sb[0].toString();
        String part2 = null;
        if ((i>0) && !sb[1].toString().isEmpty()) {
            part2 = sb[1].toString();
        }
        return new String[]{part1,part2};
    }
}
