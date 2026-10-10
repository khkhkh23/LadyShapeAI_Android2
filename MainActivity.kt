package com.ladyshape.ai

import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val Rose = Color(0xFFB84C72)
private val PaleRose = Color(0xFFFFEDF3)
private val Ink = Color(0xFF30252A)
private val Muted = Color(0xFF796C72)
private val Background = Color(0xFFFFFAFC)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LadyShapeApp() }
    }
}

@Composable
fun LadyShapeApp() {
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var height by remember { mutableFloatStateOf(165f) }
    var weight by remember { mutableFloatStateOf(75f) }
    var targetWeight by remember { mutableFloatStateOf(65f) }
    var skinTone by remember { mutableIntStateOf(2) }
    var bodyShape by remember { mutableStateOf("متوسط") }
    var clothing by remember { mutableStateOf("ملابس الجيم واليوغا") }
    var showPreview by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> photoUri = uri }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Rose,
            onPrimary = Color.White,
            secondary = Color(0xFF8D5A70),
            background = Background,
            surface = Color.White
        )
    ) {
        Scaffold(
            containerColor = Background,
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(
                        selected = !showPreview,
                        onClick = { showPreview = false },
                        icon = { Icon(Icons.Default.Face, contentDescription = null) },
                        label = { Text("جسمي") }
                    )
                    NavigationBarItem(
                        selected = showPreview,
                        onClick = { showPreview = true },
                        icon = { Icon(Icons.Default.Checkroom, contentDescription = null) },
                        label = { Text("المعاينة") }
                    )
                }
            }
        ) { padding ->
            if (showPreview) {
                PreviewScreen(
                    modifier = Modifier.padding(padding),
                    photoUri = photoUri,
                    height = height.toInt(),
                    weight = weight.toInt(),
                    targetWeight = targetWeight.toInt(),
                    bodyShape = bodyShape,
                    clothing = clothing,
                    skinTone = skinTone,
                    onBack = { showPreview = false }
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 18.dp)
                        .padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(PaleRose),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Rose)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("LadyShape AI", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
                            Text("تصوّر افتراضي وتجربة الملابس", fontSize = 13.sp, color = Muted)
                        }
                    }

                    PrivacyCard()

                    SectionTitle("1. صورة الوجه أو الرأس", "اختاري صورة واضحة من هاتفك")
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFF0DDE5)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (photoUri != null) {
                                AsyncImage(
                                    model = photoUri,
                                    contentDescription = "الصورة المختارة",
                                    modifier = Modifier.size(132.dp).clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(Modifier.height(10.dp))
                            } else {
                                Box(
                                    modifier = Modifier.size(112.dp).clip(CircleShape).background(PaleRose),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Rose, modifier = Modifier.size(38.dp))
                                }
                                Spacer(Modifier.height(10.dp))
                                Text("لم تختاري صورة بعد", color = Muted, fontSize = 14.sp)
                            }
                            Spacer(Modifier.height(12.dp))
                            Button(
                                onClick = { imagePicker.launch("image/*") },
                                colors = ButtonDefaults.buttonColors(containerColor = Rose),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Upload, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text(if (photoUri == null) "تحميل صورة من الهاتف" else "تغيير الصورة")
                            }
                        }
                    }

                    SectionTitle("2. القياسات", "الوزن محدود من 35 إلى 120 كغ")
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color(0xFFF0DDE5))
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                            SliderSetting("الطول", "${height.toInt()} سم", height, 140f, 200f) { height = it }
                            SliderSetting("الوزن الحالي", "${weight.toInt()} كغ", weight, 35f, 120f) { weight = it }
                            Divider(color = Color(0xFFF3E5EB))
                            SliderSetting("الوزن المستهدف (اختياري)", "${targetWeight.toInt()} كغ", targetWeight, 35f, 120f) { targetWeight = it }
                        }
                    }

                    SectionTitle("3. لون البشرة", "اختاري الدرجة الأقرب للصورة التي تريدينها")
                    val tones = listOf(
                        Color(0xFFF6D9C5), Color(0xFFE8B995), Color(0xFFD39A70),
                        Color(0xFFB87950), Color(0xFF8D573C), Color(0xFF5C382B)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        tones.forEachIndexed { index, tone ->
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(tone)
                                    .then(if (skinTone == index) Modifier.padding(3.dp).background(tone, CircleShape) else Modifier)
                                    .clickable { skinTone = index },
                                contentAlignment = Alignment.Center
                            ) {
                                if (skinTone == index) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = if (index > 3) Color.White else Ink)
                                }
                            }
                        }
                    }

                    SectionTitle("4. شكل الجسم", "اختاري الهيئة الافتراضية")
                    ChoiceChips(
                        options = listOf("نحيف", "متوسط", "ممتلئ", "رياضي", "منحنيات"),
                        selected = bodyShape,
                        onSelect = { bodyShape = it }
                    )

                    SectionTitle("5. الملابس", "اختاري نوع الملابس للمعاينة")
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        ClothingCard(
                            title = "الملابس الداخلية النسائية",
                            subtitle = "حمالات صدر، سراويل داخلية وأطقم متناسقة",
                            icon = Icons.Default.Favorite,
                            selected = clothing == "الملابس الداخلية النسائية",
                            onClick = { clothing = "الملابس الداخلية النسائية" }
                        )
                        ClothingCard(
                            title = "ملابس الجيم واليوغا",
                            subtitle = "Leggings، Yoga Pants، أطقم رياضية وملابس تدريب",
                            icon = Icons.Default.FitnessCenter,
                            selected = clothing == "ملابس الجيم واليوغا",
                            onClick = { clothing = "ملابس الجيم واليوغا" }
                        )
                    }

                    Button(
                        onClick = { showPreview = true },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Rose)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("إنشاء التصوّر", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        "النتيجة التصويرية تحتاج إلى محرك ذكاء اصطناعي متخصص. هذه النسخة تعرض إعداداتك فقط.",
                        color = Muted, fontSize = 12.sp, textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun PrivacyCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F8F3)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF34734B))
            Spacer(Modifier.width(9.dp))
            Column {
                Text("صورك للاستخدام الشخصي", color = Color(0xFF285B3A), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Text("لا يوجد نشر أو ملف عام داخل التطبيق.", color = Color(0xFF477657), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Ink)
        Text(subtitle, fontSize = 12.sp, color = Muted)
    }
}

@Composable
private fun SliderSetting(
    title: String,
    valueText: String,
    value: Float,
    min: Float,
    max: Float,
    onChange: (Float) -> Unit
) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(title, color = Ink, fontWeight = FontWeight.Medium)
            Text(valueText, color = Rose, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onChange,
            valueRange = min..max,
            colors = SliderDefaults.colors(thumbColor = Rose, activeTrackColor = Rose)
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(min.toInt().toString(), fontSize = 11.sp, color = Muted)
            Text(max.toInt().toString(), fontSize = 11.sp, color = Muted)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChoiceChips(options: List<String>, selected: String, onSelect: (String) -> Unit) {
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            FilterChip(
                selected = selected == option,
                onClick = { onSelect(option) },
                label = { Text(option) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PaleRose,
                    selectedLabelColor = Rose
                )
            )
        }
    }
}

@Composable
private fun ClothingCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (selected) PaleRose else Color.White),
        border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) Rose else Color(0xFFF0DDE5)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(46.dp).clip(RoundedCornerShape(14.dp)).background(if (selected) Color.White else PaleRose),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Rose)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = Ink, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(subtitle, color = Muted, fontSize = 12.sp)
            }
            RadioButton(selected = selected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = Rose))
        }
    }
}

@Composable
private fun PreviewScreen(
    modifier: Modifier,
    photoUri: Uri?,
    height: Int,
    weight: Int,
    targetWeight: Int,
    bodyShape: String,
    clothing: String,
    skinTone: Int,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "رجوع") }
            Text("معاينة التصوّر", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Ink)
        }
        Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(20.dp)) {
            Column(Modifier.fillMaxWidth().padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                if (photoUri != null) {
                    AsyncImage(
                        model = photoUri,
                        contentDescription = "صورة الرأس المختارة",
                        modifier = Modifier.size(110.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Rose, modifier = Modifier.size(90.dp))
                    Text("لم يتم اختيار صورة للوجه بعد", color = Muted)
                }
                Spacer(Modifier.height(12.dp))
                Text("إعدادات الجسم الافتراضي", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink)
                Spacer(Modifier.height(10.dp))
                SummaryRow("الطول", "$height سم")
                SummaryRow("الوزن الحالي", "$weight كغ")
                SummaryRow("الوزن المستهدف", "$targetWeight كغ")
                SummaryRow("شكل الجسم", bodyShape)
                SummaryRow("لون البشرة", "الدرجة ${skinTone + 1}")
                SummaryRow("قسم الملابس", clothing)
            }
        }
        Card(colors = CardDefaults.cardColors(containerColor = PaleRose), shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Rose)
                Text("محرك إنشاء الصور غير مفعّل بعد", fontWeight = FontWeight.Bold, color = Ink, fontSize = 16.sp)
                Text(
                    "هذه نسخة أولية من واجهة التطبيق. إعداداتك تعمل، لكن إنتاج صورة فوتوغرافية لجسم افتراضي وتجربة الملابس يحتاج إلى ربط التطبيق بنموذج توليد صور/تجربة ملابس بالذكاء الاصطناعي.",
                    color = Muted, fontSize = 13.sp
                )
                Text(
                    "مهم: الطول والوزن لا يحددان شكل الجسم بدقة؛ التصوّر المستقبلي سيكون تقريبيًا.",
                    color = Muted, fontSize = 12.sp
                )
            }
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
            Text("العودة إلى الإعدادات")
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Muted, fontSize = 13.sp)
        Text(value, color = Ink, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}
