package com.github.bytecat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.bytecat.contact.Cat
import com.github.bytecat.ext.iconRes
import com.github.bytecat.resource.R
import com.github.bytecat.vm.CatBookViewModel

@Composable
fun MainView(catBookVM: CatBookViewModel, onItemClick: (cat: Cat) -> Unit) {
    val myCat = catBookVM.myCat.value
    Column {
        if (myCat != null) {
            Spacer(modifier = Modifier.height(16.dp))
            MyCatView(myCat = myCat)
        }
        if (catBookVM.cats.isEmpty()) {
            EmptyView()
        } else {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1F, true),
                    thickness = 0.5.dp,
                    color = myCatTheme().dividerColor
                )
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = "${catBookVM.cats.size} cats",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 10.sp
                )
                Divider(
                    modifier = Modifier.weight(1F, true),
                    thickness = 0.5.dp,
                    color = myCatTheme().dividerColor
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                itemsIndexed(catBookVM.cats) { index, item ->
                    CatItemView(cat = item, onItemClick)
                    if (index < catBookVM.cats.lastIndex) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyView() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            modifier = Modifier.width(240.dp),
            painter = painterResource(resourcePath = R.drawable.img_no_cats),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400,
            fontSize = 24.sp,
            color = myCatTheme().myCatNameColor,
            text = R.string.empty_cats
        )
    }
}

@Composable
fun MyCatView(myCat: Cat) {
    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(
                color = myCatTheme().myCatBackgroundColor,
                shape = RoundedCornerShape(corner = CornerSize(16.dp))
            )
            .border(
                width = 1.dp,
                color = myCatTheme().myCatBorderColor,
                shape = RoundedCornerShape(corner = CornerSize(16.dp))
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            painter = painterResource(myCat.platform.iconRes),
            colorFilter = ColorFilter.tint(color = myCatTheme().itemIconTintColor),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = myCat.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = myCat.ipAddress,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp
        )
    }
}

@Composable
fun CatItemView(cat: Cat, onClick: (cat: Cat) -> Unit) {

    val roundedCornerShape = RoundedCornerShape(corner = CornerSize(16.dp))
    Row(
        modifier = Modifier
            .height(56.dp)
            .padding(horizontal = 16.dp)
            .clip(shape = roundedCornerShape)
            .clickable {
                onClick.invoke(cat)
            }
            .background(color = myCatTheme().itemBackgroundColor)
            .border(
                width = 0.5.dp,
                color = myCatTheme().itemBorderColor,
                shape = roundedCornerShape
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(12.dp))
        Image(
            painter = painterResource(cat.platform.iconRes),
            modifier = Modifier
                .size(40.dp)
                .padding(4.dp),
            colorFilter = ColorFilter.tint(myCatTheme().itemIconTintColor),
            contentDescription = ""
        )
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1F)) {
            Text(
                text = cat.name,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = cat.ipAddress,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Light,
                fontSize = 10.sp
            )
        }
        Spacer(Modifier.width(8.dp))
        Image(
            painter = painterResource(R.drawable.ic_file_send_outline),
            modifier = Modifier
                .size(32.dp)
                .clip(roundedCornerShape)
                .clickable {
                    onClick.invoke(cat)
                }
                .padding(4.dp),
            colorFilter = ColorFilter.tint(myCatTheme().itemIconTintColor),
            contentDescription = ""
        )
        Spacer(Modifier.width(8.dp))
        Image(
            painter = painterResource(R.drawable.ic_message_fast_outline),
            modifier = Modifier
                .size(32.dp)
                .clip(roundedCornerShape)
                .clickable {
                    onClick.invoke(cat)
                }
                .padding(4.dp),
            colorFilter = ColorFilter.tint(myCatTheme().itemIconTintColor),
            contentDescription = ""
        )
        Spacer(Modifier.width(12.dp))
    }
}