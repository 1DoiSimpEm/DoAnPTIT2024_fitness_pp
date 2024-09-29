package ptit.vietpq.fitnessapp.presentation.main.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

enum class TopLevelDestination(
  override val route: String,
  override val destination: String,
  @DrawableRes val iconResourceId: Int,
  @DrawableRes val iconResourceSelect: Int,
  @StringRes val textResourceId: Int,
) : FitnessNavigationDestination {
//  Scan(
//    route = ScanDestination.route,
//    destination = ScanDestination.destination,
//    iconResourceId = R.drawable.ic_scan,
//    iconResourceSelect = R.drawable.ic_scan_select,
//    textResourceId = R.string.scan_tab,
//  ),
//  Create(
//    route = CreateDestination.route,
//    destination = CreateDestination.destination,
//    iconResourceId = R.drawable.ic_create,
//    iconResourceSelect = R.drawable.ic_create_select,
//    textResourceId = R.string.create,
//  ),
//  History(
//    route = HistoryDestination.route,
//    destination = HistoryDestination.destination,
//    iconResourceId = R.drawable.ic_history,
//    iconResourceSelect = R.drawable.ic_history_select,
//    textResourceId = R.string.history_tab,
//  ),
//  Settings(
//    route = SettingDestination.route,
//    destination = SettingDestination.destination,
//    iconResourceId = R.drawable.ic_setting,
//    iconResourceSelect = R.drawable.ic_setting_select,
//    textResourceId = R.string.settings__settings,
//  ),
}
