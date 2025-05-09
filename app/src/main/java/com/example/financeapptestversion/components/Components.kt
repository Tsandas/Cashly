package com.example.financeapptestversion.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.financeapptestversion.model.Transaction
import com.example.financeapptestversion.navigation.AppScreens
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.financeapptestversion.R

@Composable
fun AppLogo(){
    Image(
        painter = painterResource(id = R.drawable.cashly_logo),
        contentDescription = "App Logo",
        modifier = Modifier
            .size(64.dp)
            .padding(8.dp)
    )
}

@Composable
fun FinanceLogo(modifier: Modifier = Modifier) {
    Text(
        text = "Cashly",
        style = MaterialTheme.typography.displayLarge,
        modifier = modifier.padding(bottom = 16.dp),
        color = Color.Green.copy(alpha = 0.5f)
    )
}

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        valueState.value,
        onValueChange = {
            valueState.value = it
        },
        label = { Text(text = labelId, color = Color.Black) },
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxSize(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
    )
}


@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    enabled: Boolean,
    labelId: String,
    passwordVisability: MutableState<Boolean>,
    onAction: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Done
) {

    val visualTransformation =
        if (passwordVisability.value) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = {
            passwordState.value = it
        },
        label = { Text(text = labelId, color = Color.Black) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        trailingIcon = {
            PasswordVisibility(passwordVisability = passwordVisability)
        },
        keyboardActions = onAction
    )
}

@Composable
fun PasswordVisibility(passwordVisability: MutableState<Boolean>) {

    val visible = passwordVisability.value
    IconButton(onClick = { passwordVisability.value = !visible }) {
        Icons.Default.Close
    }

}


@Composable
fun TitleSection(modifier: Modifier = Modifier, label: String) {
    Surface(
        modifier = Modifier.padding(start = 5.dp, top = 1.dp)
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceAppBar(
    title: String,
    icon: ImageVector? = null,
    navController: NavController,
    onIconClicked: () -> Unit = {},
    infoIconAction: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        navigationIcon = {
            if (icon != null) {
                IconButton(onClick = { onIconClicked() }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        actions = {
            if (infoIconAction != null) {
                IconButton(onClick = infoIconAction) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun BottomBar(
    navController: NavController,
    homeScreen: Boolean = false,
    stocksScreen: Boolean = false
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationBarItem(
            selected = homeScreen,
            onClick = { navController.navigate(AppScreens.HomeScreen.name) },
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            },
            label = { Text("Home", color = MaterialTheme.colorScheme.onPrimary) }
        )
        NavigationBarItem(
            selected = stocksScreen,
            onClick = { navController.navigate(AppScreens.StocksScreen.name) },
            icon = {
                Icon(
                    Icons.Default.ShowChart,
                    contentDescription = "Stocks",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            },
            label = { Text("Stocks", color = MaterialTheme.colorScheme.onPrimary) }
        )
    }
}


@Composable
fun RoundedButton(
    label: String = "More",
    radius: Dp = 24.dp,
    modifier: Modifier = Modifier,
    onPressDetails: () -> Unit = {}
) {
    Button(
        onClick = onPressDetails,
        shape = RoundedCornerShape(radius),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        modifier = modifier
            .height(48.dp)
            .widthIn(min = 100.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp)
        )
    }
}


fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDialogHomeScreen(
    title: String,
    openDialog: MutableState<Boolean>,
    onYesPressed: () -> Unit,
    content: @Composable () -> Unit
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    content()
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { onYesPressed.invoke() },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Yes",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { openDialog.value = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "No",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        )
    }
}

//
//@Composable
//fun TransactionItems(transaction: Transaction, onDeleteClicked: (Transaction) -> Unit, onCardClicked: (Transaction) -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable {
//                onCardClicked(transaction)
//            }
//            .padding(vertical = 4.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(2.dp)) {
//        Row(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            val formatter = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())
//            val formattedDate = formatter.format(transaction.entryDate)
//            Column {
//                Text(transaction.title, fontWeight = FontWeight.Bold, color = Color.Black)
//                Text(
//                    text = formattedDate,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = Color.Black
//                )
//            }
//            Text(
//                text = "$${transaction.amount}",
//                fontWeight = FontWeight.Bold,
//                color = if (transaction.isExpense) Color(0xFFF44336) else Color(0xFF4CAF50)
//            )
//            Icon(
//                modifier = Modifier.clickable {
//                    onDeleteClicked(transaction)
//                },
//                imageVector = Icons.Default.Delete,
//                contentDescription = "Delete Icon",
//                tint = Color.Gray
//            )
//        }
//    }
//}

@Composable
fun TransactionItem(
    transaction: Transaction,
    onDeleteClicked: (Transaction) -> Unit,
    onCardClicked: (Transaction) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClicked(transaction) }
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val formatter = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())
            val formattedDate = formatter.format(transaction.entryDate)

            Column(
                modifier = Modifier.weight(1f) // allows space for the right section
            ) {
                Text(
                    text = transaction.title,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${transaction.amount}",
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.isExpense) Color(0xFFF44336) else Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    modifier = Modifier.clickable {
                        onDeleteClicked(transaction)
                    },
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Icon",
                    tint = Color.Gray
                )
            }
        }
    }
}
