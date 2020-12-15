package com.marvelsample.app.di

import com.marvelsample.app.BuildConfig
import com.marvelsample.app.core.repository.CharacterDetailsRepository
import com.marvelsample.app.core.repository.CharactersListRepository
import com.marvelsample.app.core.repository.memory.ItemMemoryRepository
import com.marvelsample.app.core.repository.memory.PagedCollectionMemoryRepository
import com.marvelsample.app.core.repository.model.characters.Character
import com.marvelsample.app.core.repository.network.*
import com.marvelsample.app.core.usecases.characterdetails.CharacterDetailsUseCase
import com.marvelsample.app.core.usecases.characterslist.CharactersListUseCase
import com.marvelsample.app.ui.utils.imageloader.CoilImageLoader
import com.marvelsample.app.ui.utils.imageloader.ImageLoader
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.*
import java.util.concurrent.TimeUnit

val appModule = DI.Module("app") {
    bind<ImageLoader>() with singleton {
        CoilImageLoader(instance())
    }

    bind<String>("privateKey") with singleton { BuildConfig.MARVEL_PRIVATE_KEY }
    bind<String>("publicKey") with singleton { BuildConfig.MARVEL_API_KEY }
}

val networkModule = DI.Module("network") {
    bind() from singleton {
        OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()
    }
}

val repositoryModule = DI.Module("repositories") {
    bind<PagedCollectionMemoryRepository<Character>>() with singleton {
        PagedCollectionMemoryRepository()
    }
    bind<ItemMemoryRepository<Int, Character>>() with singleton {
        ItemMemoryRepository()
    }

    bind<CharactersListRepository>() with singleton {
        CharactersListRepository(
            instance(),
            instance(),
        )
    }
    bind<CharacterDetailsRepository>() with singleton {
        CharacterDetailsRepository(
            instance(),
            instance(),
        )
    }

    bind<CharacterDetailsNetworkRepository>() with provider {
        CharacterDetailsNetworkRepositoryImpl(
            instance(),
            instance("privateKey"),
            instance("publicKey")
        )
    }

    bind<CharacterListNetworkRepository>() with provider {
        CharacterListNetworkRepositoryImpl(
            instance(),
            instance("privateKey"),
            instance("publicKey")
        )
    }
}

val userCases = DI.Module("useCases") {
    bind<CharactersListUseCase>() with provider {
        CharactersListUseCase(instance())
    }
    bind<CharacterDetailsUseCase>() with provider {
        CharacterDetailsUseCase(instance())
    }
}

val systemModule = DI.Module("system") {
    bind<ApiService>() with provider { ApiClient(instance()).getService() }
}

