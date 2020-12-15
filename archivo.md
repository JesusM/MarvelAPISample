This sample project uses these elements to implement the solution provided:
  - Kotlin as main development language.
  - Coroutines and flows for asynchronous code handling.
  - For the "character list" UI, usage of paging3 Android library which supports flow and direct connection with RecyclerView adapter (for easier usage).
  - ViewModel and LiveData as main components linked to UI elements.
  - KodeIn (https://docs.kodein.org/kodein-di/7.1/framework/android.html) as dependency injection library.
  - View Binding (https://developer.android.com/topic/libraries/view-binding) to inflate and specify UI-related elements.
  - Coil (https://coil-kt.github.io/coil/) as image loader.

The package structure of the project is:
  - ui: for UI-related elements.
  - di: for dependency injection modules.
  - core: contains domain-related elements like models and repositories.

In terms of elements, this is a simpler picture of "flow" structure:
  UI -> Use case -> Repositories

The UI structure is just 2 simple activities. I've used such approach cause its simplicity and flexibility (home could start containing other complex elements).
For the use cases, for now they're just repository consumers but the could potentially include more logic related to the UI component they're linked to.
The repositories just take care of content fetching.