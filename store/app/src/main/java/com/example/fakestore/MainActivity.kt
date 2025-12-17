package com.example.fakestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fakestore.ui.theme.FakestoreTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.google.gson.annotations.SerializedName

// ----------------------
// 데이터 클래스 & DTO
// ----------------------
data class Product(
    val id: Int,
    val name: String,
    val type: String,   // 우송대 가방 / 우송대 램프
    val price: Double,
    val image: String
)

data class FakeStoreProductDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("price") val price: Double,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("image") val image: String
)

// DTO → Product 변환
fun FakeStoreProductDto.toProduct(): Product {
    val type = when {
        category.contains("electronics", ignoreCase = true) ||
                title.contains("lamp", ignoreCase = true) ||
                title.contains("light", ignoreCase = true) ->
            "우송대 굿즈"
        else -> "우송대 굿즈"
    }

    val displayName = "$type - $title"

    return Product(
        id = id,
        name = displayName,
        type = type,
        price = price,
        image = image
    )
}

// ----------------------
// Retrofit 정의
// ----------------------
interface FakeStoreApi {
    @GET("products")
    suspend fun getProducts(): List<FakeStoreProductDto>
}

// Retrofit 싱글톤
object FakeStoreClient {
    private const val BASE_URL = "https://fakestoreapi.com/"

    val api: FakeStoreApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FakeStoreApi::class.java)
    }
}

// ----------------------
// Activity
// ----------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FakestoreTheme {
                FakeStoreScreen()
            }
        }
    }
}

// ----------------------
// UI
// ----------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FakeStoreScreen() {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // 앱 열릴 때 한 번만 실행
    LaunchedEffect(Unit) {
        try {
            isLoading = true
            error = null

            val result = withContext(Dispatchers.IO) {
                FakeStoreClient.api.getProducts().map { it.toProduct() }
            }

            products = result

        } catch (e: Exception) {
            error = e.message ?: "알 수 없는 오류 발생"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("우송대 스토어") }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (!isLoading && error == null) {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(products) { product ->
                        ProductCard(product)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(8.dp)) {

            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = product.type,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "₩${product.price}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
