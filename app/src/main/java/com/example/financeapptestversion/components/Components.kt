package com.example.financeapptestversion.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.financeapptestversion.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth

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
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxSize(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction
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
        label = { Text(text = labelId) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
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
                fontSize = 19.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Left
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceAppBar(
    title: String,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavController,
    onBackArrowClicked: () -> Unit = {}
) {

    TopAppBar(
        title = {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showProfile) {
                Icon(
                    imageVector = Icons.Default.Money,
                    contentDescription = "Money Icon",
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
                        .scale(0.9f)
                )
            }
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Arrow Back",
                    tint = Color.Green,
                    modifier = Modifier.clickable { onBackArrowClicked.invoke() })
            }

            Spacer(modifier = Modifier.width(40.dp))

            Text(
                text = title,
                color = Color.Green.copy(alpha = 0.9f),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )


        }
    }, actions = {
        IconButton(onClick = {
            FirebaseAuth.getInstance().signOut().run {
                navController.navigate(AppScreens.LoginScreen.name)
            }
        }) {
            if (showProfile) {
                Icon(
                    imageVector = Icons.Filled.Logout, contentDescription = "Logout"
                )
            } else {
                Box() {}
            }
        }
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent
    )
    )

}


@Composable
fun FABContent(onTap: () -> Unit) {

    FloatingActionButton(
        onClick = { onTap() }, shape = RoundedCornerShape(50.dp), containerColor = Color(0xFF009688)
    ) {

        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Stock",
            tint = Color(0xFF8BC34A)
        )

    }

}


@Composable
fun DetailsButton(
    label: String = "More", radius: Int = 29, onPressDetails: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.clip(
            RoundedCornerShape(
                topStartPercent = radius, topEndPercent = radius
            )
        ), color = Color(0xFF009688)
    ) {
        Column(
            modifier = Modifier
                .width(90.dp)
                .heightIn(40.dp)
                .clickable { onPressDetails.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = TextStyle(color = Color.White, fontSize = 15.sp))
        }
    }

}