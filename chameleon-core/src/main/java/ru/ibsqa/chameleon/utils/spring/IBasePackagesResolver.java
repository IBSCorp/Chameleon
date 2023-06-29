package ru.ibsqa.chameleon.utils.spring;

import java.util.List;

/**
 * Поставщик списка пакетов для сканирования классов
 */
public interface IBasePackagesResolver {
    List<String> getBasePackages();
}
