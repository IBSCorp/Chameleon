package ru.ibsqa.chameleon.utils.xml;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.Resource;

@AllArgsConstructor
@Builder
public class XmlResource {
    @Getter
    private String namespace;
    @Getter
    private String root;
    @Getter
    private Resource resource;
}
