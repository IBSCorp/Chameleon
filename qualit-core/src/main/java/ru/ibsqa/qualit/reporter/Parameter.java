package ru.ibsqa.qualit.reporter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Parameter {
    @XmlElement
    private String key;
    @XmlElement
    private String value;
}
