package com.marvelsample.app.di

import com.marvelsample.app.BuildConfig
import com.marvelsample.app.core.model.Character
import com.marvelsample.app.core.repository.memory.ItemMemoryRepository
import com.marvelsample.app.core.repository.memory.PagedCollectionMemoryRepository
import com.marvelsample.app.core.repository.network.ApiClient
import com.marvelsample.app.core.usecases.characterdetails.CharacterDetailsUseCase
import com.marvelsample.app.core.usecases.characterdetails.repository.CharacterDetailsRepository
import com.marvelsample.app.core.usecases.characterdetails.repository.CharacterDetailsRepositoryImpl
import com.marvelsample.app.core.usecases.characterdetails.repository.memory.CharacterDetailMemoryRepository
import com.marvelsample.app.core.usecases.characterdetails.repository.memory.CharacterDetailMemoryRepositoryImpl
import com.marvelsample.app.core.usecases.characterdetails.repository.network.CharacterDetailsNetworkRepository
import com.marvelsample.app.core.usecases.characterdetails.repository.network.CharacterDetailsNetworkRepositoryImpl
import com.marvelsample.app.core.usecases.characterslist.CharactersListUseCase
import com.marvelsample.app.core.usecases.characterslist.repository.CharactersListRepository
import com.marvelsample.app.core.usecases.characterslist.repository.CharactersListRepositoryImpl
import com.marvelsample.app.core.usecases.characterslist.repository.network.CharacterListNetworkRepository
import com.marvelsample.app.core.usecases.characterslist.repository.network.CharacterListNetworkRepositoryImpl
import com.marvelsample.app.ui.characterdetails.DetailViewModel
import com.marvelsample.app.ui.characterslist.CharactersListViewModel
import com.marvelsample.app.ui.utils.imageloader.CoilImageLoader
import com.marvelsample.app.ui.utils.imageloader.ImageLoader
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val appModule = module {
    single<ImageLoader> {
        CoilImageLoader(get())
    }

    single(named("privateKey")) { BuildConfig.MARVEL_PRIVATE_KEY }
    single(named("publicKey")) { BuildConfig.MARVEL_API_KEY }
}

val networkModule = module {
    single {
        OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()
    }
}

val repositoryModule = module {
    single<PagedCollectionMemoryRepository<Character>>(named("charactersMemoryRepository")) {
        PagedCollectionMemoryRepository()
    }
    single<CharacterDetailMemoryRepository>(named("characterMemoryRepository")) {
        CharacterDetailMemoryRepositoryImpl(ItemMemoryRepository())
    }

    single<CharactersListRepository>(named("charactersListRepository")) {
        CharactersListRepositoryImpl(
            get(named("charactersMemoryRepository")),
            get(named("charactersNetworkRepository")),
        )
    }
    single<CharacterDetailsRepository>(named("characterDetailsRepository")) {
        CharacterDetailsRepositoryImpl(
            get(named("characterMemoryRepository")),
            get(named("characterNetworkRepository")),
        )
    }

    single<CharacterDetailsNetworkRepository>(named("characterNetworkRepository")) {
        CharacterDetailsNetworkRepositoryImpl(
            get(),
            get(named("privateKey")),
            get(named("publicKey"))
        )
    }

    single<CharacterListNetworkRepository>(named("charactersNetworkRepository")) {
        CharacterListNetworkRepositoryImpl(
            get(),
            get(named("privateKey")),
            get(named("publicKey"))
        )
    }
}

val userCases = module {
    single(named("characterslistUseCase")) {
        CharactersListUseCase(get(named("charactersListRepository")))
    }
    single(named("detailUseCase")) {
        CharacterDetailsUseCase(get(named("characterDetailsRepository")))
    }
}

val viewModels = module {
    viewModel(named("detailViewModel")) {
        DetailViewModel(get(named("detailUseCase")))
    }

    viewModel(named("charactersListViewModel")) {
        CharactersListViewModel(get(named("characterslistUseCase")))
    }
}

val systemModule = module {
    single { ApiClient(get()).getService() }
}

