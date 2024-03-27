import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import cafe.adriel.voyager.navigator.Navigator
import com.example.myapplication.ui.theme.AppTheme
import com.valentinilk.shimmer.LocalShimmerTheme
import com.valentinilk.shimmer.defaultShimmerTheme
import data.di.dataModule
import domain.di.domainModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.startKoin
import ui.di.uiModule
import ui.screens.main.MainScreen


@Composable
@Preview
fun App() {
    startKoin {
        modules(dataModule, uiModule, domainModule)
    }
    val yourShimmerTheme = defaultShimmerTheme.copy()

    CompositionLocalProvider(
        LocalShimmerTheme provides yourShimmerTheme
    ) {

        AppTheme {
            Surface {
                Navigator(MainScreen)

            }
        }
    }
}