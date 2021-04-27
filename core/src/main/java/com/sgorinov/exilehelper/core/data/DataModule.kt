package com.sgorinov.exilehelper.core.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sgorinov.exilehelper.core.domain.ICoreRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.decodeCertificatePem
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

internal val certificate = """-----BEGIN CERTIFICATE-----
MIIFzzCCA7egAwIBAgIEQhQczjANBgkqhkiG9w0BAQwFADCBlzELMAkGA1UEBhMC
UlUxEzARBgNVBAgTCk9ybG92c2theWExDTALBgNVBAcTBE9yZWwxETAPBgNVBAoT
CFNHb3Jpbm92MRIwEAYDVQQLEwlEZXZlbG9wZXIxPTA7BgNVBAMTNGVjMi0xOC0x
NTYtMTI2LTQ0LmV1LWNlbnRyYWwtMS5jb21wdXRlLmFtYXpvbmF3cy5jb20wHhcN
MjEwNDI3MTQ1MTIzWhcNMzUwMTA0MTQ1MTIzWjCBlzELMAkGA1UEBhMCUlUxEzAR
BgNVBAgTCk9ybG92c2theWExDTALBgNVBAcTBE9yZWwxETAPBgNVBAoTCFNHb3Jp
bm92MRIwEAYDVQQLEwlEZXZlbG9wZXIxPTA7BgNVBAMTNGVjMi0xOC0xNTYtMTI2
LTQ0LmV1LWNlbnRyYWwtMS5jb21wdXRlLmFtYXpvbmF3cy5jb20wggIiMA0GCSqG
SIb3DQEBAQUAA4ICDwAwggIKAoICAQCNqiCFNf05z8pOfAi1EhCEKxn2mKrMpHD9
LBiNyLSgiSHXjAmMNBVLSm5wrbmqLWC/0Fdx0UoEBNw25ii2Z+OzZrrPrkAyACXF
+EDKayd94+MVZ6b/G5c77uR9b7/dl4M2CurDY5Z1WXB+8TqXnrnWOZqWhThJ4GJ9
8y1f2MNuZCdsV//Rww1D51avoIElkDRRBQcxkrFXiU7/Ul9woOY/k/8V7RYPO3Wn
PJyQrwGL1db50iMxQ3iEmOK8GZU8kt31oUGz50PGL+z+WkqHQK2P8hLBrSalEz3D
K3+DSkWw/jnVgd3d20ADITIDH2huonZG+tYTdGek4x6JO/qLgW9vzi2EkYuOE6Ja
DvPloqy764jOF35saUCaqLS9DaaRfi3S/cGsyWW5tr/eqv9DozC5C/wNHj7Y56XO
LUBAiOvzkfoUa/qtzoh8T2KKd1k0nKI2j5x1hM+eWcPrC9TAOO0UjyLl17mxjzO7
uQnORl6IWW6hNP3b1BxwDSIp6Ge8wtcK6nx3BeT0hvTsRF2iQdIRJ8AfFdpruaj6
OHnvZ6GEipQPkmTuk6elMxGRI67TpIlRN/ANJTDeHTWaTdBZ6ATVBdSWWfZ2Eg+E
vLOqNx/EYsQVUD9hS+3hksakry+VWzRdTVsFejbjc3QW7cXR2dIoAxgweEkuvVp0
OE1Cyc0lUwIDAQABoyEwHzAdBgNVHQ4EFgQUP3wEaOnij5olkesRNZF5RTi3RZ0w
DQYJKoZIhvcNAQEMBQADggIBABDejNKjssdj8h3w+POQxRA7z+pLvPa80ccbo9tr
HZlsKUJDxfgg9lR/GM+3RlNyGN9tiD0+FJiaJ+jgNPbVhjnfJSKrT9SanPGwp2yT
1btUQfPNRUiIOdLCQvSanwYx/rXi8VHIaVdJbC48n/+3E5hRZhOad1u3vz4GDJPu
KtLo1iM1d4It/Ar4ddwWx51YHblL15+HG9Di/BGtGsgMt7ZlS3z40bynVY1xm2CK
Y6DLCcZYa9QbfTL3suF5Wj63vj4Le6eu/JRNOdN1ZUblAysvGHv/23kUqUAD3Pnj
J83Nz3xoEuc3zmAwqW0yJB9YXuQS40AlElOyXHB8A4BnBDm2cSWbPU8ZKRt9p9iR
pRtqL8XUE6mP8ndziB4y8MGwn6IIjFf23mLAbu+rxyo+556HQty0GsfOF0KJVVtE
09xTJJC2ku0i5E2X7wr3o0cFc3QV96HMoIZaQFPzAB6NvKmw/bZFHS6l6QzJVgrH
qbOViAUd3ZOvDSh9haK2NJjpgfXgOrDWonRhozC5ZTaOQi8et3BMU1bLyZKhNUuF
DVH5HMlwRnu+RNuz4OASc1ZfCrLeRnXJuC7UiD2ngV8YsILNBYFXkH2fJKiIUk+W
35zACuf+x95i0p60YWcqLFSuyMynV0HfyzIORrsobHkBnYlTBM+SMf9YGGBbWOXy
z/qf
-----END CERTIFICATE-----""".trimIndent().decodeCertificatePem()

internal val certificates =
    HandshakeCertificates.Builder().addTrustedCertificate(certificate).build()

internal val customOkHttpClient = OkHttpClient
    .Builder()
    .sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager)
    .hostnameVerifier { hostname, _ ->
        hostname == "ec2-18-156-126-44.eu-central-1.compute.amazonaws.com"
    }
    .build()

val dataModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://www.pathofexile.com/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(StaticApi::class.java)
    } bind StaticApi::class
    single {
        Retrofit.Builder()
            .baseUrl("https://ec2-18-156-126-44.eu-central-1.compute.amazonaws.com/")//18.156.126.44//10.0.2.2
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(customOkHttpClient)
            .build()
            .create(ExileHelperApi::class.java)
    } bind ExileHelperApi::class
    single {
        CoreRepository(get(), get())
    } bind ICoreRepository::class
}