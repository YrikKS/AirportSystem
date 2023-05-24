package ru.nsu.group20211.airport_system.di.app_module

import dagger.Module
import dagger.Provides
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        userHolder: UserModule.UserHolder,
        urlProvider: UrlModule.UrlProvider
    ): DriverContainer {
        return DriverContainer(userHolder, urlProvider)
    }

    class DriverContainer(
        private val userHolder: UserModule.UserHolder,
        private val urlProvider: UrlModule.UrlProvider
    ) {
        var connection: Connection? = null

        fun connect(): Statement {
            if (connection == null) {
                Class.forName("oracle.jdbc.driver.OracleDriver")
                connection = DriverManager.getConnection(
                    urlProvider.url,
                    userHolder.userName,
                    userHolder.pass
                )
            }
            return connection!!.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE
            )
        }
    }
}