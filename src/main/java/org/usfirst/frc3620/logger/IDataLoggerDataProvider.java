package org.usfirst.frc3620.logger;

import java.util.function.Supplier;

/**
 * This is the interface for objects that will provide values when
 * logging. Since this is just an extension of the {@link java.util.function.Supplier}
 * interface (which just defines get()), a lambda can be used here to implementation
 * Supplier's get method.
 *
 */
public interface IDataLoggerDataProvider extends Supplier<Object> {
}
