package ru.ibsqa.qualit.definitions.repository.data;

import ru.ibsqa.qualit.definitions.repository.IRepositoryData;
import ru.ibsqa.qualit.definitions.repository.IRepositoryElement;
import lombok.Getter;
import lombok.ToString;
import ru.ibsqa.qualit.definitions.repository.MetaPriority;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.NoSuchElementException;

@ToString
@XmlRootElement(name = "DataLooks")
@MetaPriority(ConfigurationPriority.LOW)
public class DataLooks implements IRepositoryData, IRepositoryElementData {

    @Getter
    @XmlElement(name = "DataLook")
    private List<DataLook> dataLooks;

    @Override
    public DataLook pickElement(String name, Class elementType) {
        try {
            return dataLooks
                    .stream()
                    .filter(item -> DataLook.class == elementType && (null == name && null == item.getName()) || (null != item.getName() && item.getName().equals(name)))
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public <E extends IRepositoryElement> List<E> pickAllElements() {
        return (List<E>) dataLooks;
    }
}
