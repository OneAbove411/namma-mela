package com.paraminnovation.nammamela

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.paraminnovation.nammamela.payment.RazorpayHandler
import com.paraminnovation.nammamela.ui.screens.CastScreen
import com.paraminnovation.nammamela.ui.screens.FanWallScreen
import com.paraminnovation.nammamela.ui.screens.ManagerScreen
import com.paraminnovation.nammamela.ui.screens.PlayScreen
import com.paraminnovation.nammamela.ui.screens.SeatMapScreen
import com.paraminnovation.nammamela.ui.screens.TicketScreen
import com.paraminnovation.nammamela.ui.theme.BrandRed
import com.paraminnovation.nammamela.ui.theme.BrandRedSoft
import com.paraminnovation.nammamela.ui.theme.NammaMelaTheme
import com.paraminnovation.nammamela.ui.theme.NeutralBg
import com.paraminnovation.nammamela.ui.theme.SurfaceWhite
import com.paraminnovation.nammamela.ui.theme.TextSecondary
import com.paraminnovation.nammamela.ui.viewmodel.AppViewModel
import com.razorpay.PaymentResultListener

class MainActivity : ComponentActivity(), PaymentResultListener {

    private val viewModel: AppViewModel by viewModels {
        AppViewModel.Factory(application as NammaMelaApp)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RazorpayHandler.preload(this)
        setContent {
            NammaMelaTheme { AppRoot(viewModel) }
        }
    }

    // === Razorpay callbacks ===
    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        viewModel.onPaymentSuccess(razorpayPaymentID)
    }

    override fun onPaymentError(code: Int, response: String?) {
        viewModel.onPaymentCancelled()
    }
}

private data class Tab(
    val route: String,
    val labelRes: Int,
    val iconActive: ImageVector,
    val iconInactive: ImageVector
)

private val tabs = listOf(
    Tab("play", R.string.tab_play, Icons.Filled.Star, Icons.Filled.Star),
    Tab("cast", R.string.tab_cast, Icons.Filled.Person, Icons.Filled.Person),
    Tab("seats", R.string.tab_seats, Icons.Filled.ShoppingCart, Icons.Filled.ShoppingCart),
    Tab("fanwall", R.string.tab_fanwall, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
)

private val tabRoutes = tabs.map { it.route }.toSet()

@Composable
private fun AppRoot(viewModel: AppViewModel) {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route ?: "play"
    val showBottomBar = currentRoute in tabRoutes

    // React to a newly persisted ticket → navigate to TicketScreen.
    val newTicketId by viewModel.newTicketId.collectAsState()
    LaunchedEffect(newTicketId) {
        val id = newTicketId
        if (id != null) {
            viewModel.consumeNewTicket()
            nav.navigate("ticket/$id")
        }
    }

    Scaffold(
        containerColor = NeutralBg,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = SurfaceWhite,
                    tonalElevation = 8.dp
                ) {
                    tabs.forEach { tab ->
                        val selected = currentRoute == tab.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (currentRoute == tab.route) return@NavigationBarItem
                                nav.navigate(tab.route) {
                                    // Pop everything above the start destination so
                                    // tapping a tab always lands cleanly on that tab.
                                    popUpTo(nav.graph.findStartDestination().id) {
                                        inclusive = false
                                        saveState = false
                                    }
                                    launchSingleTop = true
                                    restoreState = false
                                }
                            },
                            icon = {
                                Icon(
                                    if (selected) tab.iconActive else tab.iconInactive,
                                    contentDescription = null
                                )
                            },
                            label = { Text(stringResource(tab.labelRes), style = MaterialTheme.typography.labelMedium) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BrandRed,
                                selectedTextColor = BrandRed,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = BrandRedSoft
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = "play",
            modifier = Modifier
                .padding(padding)
                .background(NeutralBg)
        ) {
            composable("play") {
                PlayScreen(
                    viewModel = viewModel,
                    onBookSeatsClick = { nav.navigate("seats") },
                    onManagerClick = { nav.navigate("manager") }
                )
            }
            composable("cast") { CastScreen(viewModel) }
            composable("seats") { SeatMapScreen(viewModel) }
            composable("fanwall") { FanWallScreen(viewModel) }
            composable("manager") {
                ManagerScreen(viewModel = viewModel, onExit = { nav.popBackStack() })
            }
            composable(
                route = "ticket/{ticketId}",
                arguments = listOf(navArgument("ticketId") { type = NavType.LongType })
            ) { entry ->
                val id = entry.arguments?.getLong("ticketId") ?: 0L
                TicketScreen(
                    viewModel = viewModel,
                    ticketId = id,
                    onDone = {
                        // Pop back to play tab cleanly
                        nav.popBackStack(route = "play", inclusive = false)
                    }
                )
            }
        }
    }
}

