package tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.ibsqa.qualit.definitions.repository.IRepositoryManager;
import ru.ibsqa.qualit.definitions.repository.selenium.elements.MetaPage;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class SearchRepositoryTest {

    @Autowired
    IRepositoryManager repositoryManager;

    @Test
    public void searchRepositoryTest(){
        assertNotEquals(repositoryManager.pickAllElements().size(),0);
        log.info(repositoryManager.pickElement("Стартовая страница Яндекс", MetaPage.class).toString());
    }
}