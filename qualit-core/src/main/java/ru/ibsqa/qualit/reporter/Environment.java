package ru.ibsqa.qualit.reporter;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "environment")
@NoArgsConstructor
public class Environment {
    @XmlElement(name = "parameter")
    @Getter
    private List<Parameter> parameters = new ArrayList<>();
}
