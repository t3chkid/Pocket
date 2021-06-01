package com.example.pocket.data.network

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.example.pocket.utils.getDownloadedResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.URL


interface Network {
    suspend fun fetchWebsiteContentTitle(url: String): String
    suspend fun downloadImage(url: String): Drawable?
    suspend fun downloadFavicon(urlString: String): Drawable?
}

class PocketNetwork(
    private val mContext: Context,
    private val mDefaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : Network {
    /**
     * Gets the content of the title tag of the [url]
     * @param url The string representation of the url
     * @return The content of the title tag of the website.
     */
    override suspend fun fetchWebsiteContentTitle(url: String): String =
        withContext(mDefaultDispatcher) { Jsoup.connect(url).get().title() }

    /**
     * Tries to download the image from the 'og:image' open graph and uses glide to get the
     * drawable.
     * @param url the complete url of the website
     * @return null if some error occurred while downloading
     */
    override suspend fun downloadImage(url: String): Drawable? =
        getImageUrl(url)?.let {
            Glide.with(mContext)
                .asDrawable()
                .load(it)
                .getDownloadedResource()
        }


    /**
     * Tries to get the url of the main image from the open graph meta tags in the html
     * document.If it cannot find the tag,it returns null.
     * @param url the complete url of the page
     * @return the url of the image
     */
    private suspend fun getImageUrl(url: String): String? = withContext(mDefaultDispatcher) {
        val document = Jsoup.connect(url).get()

        //selecting all the meta elements
        val metaElements = document.select("meta")

        //selecting all meta graph tags
        val openGraphElements = metaElements.filter { it.attr("property").contains("og:") }

        //selecting the 'og:image' tag
        val ogImageTag = openGraphElements.find { it.attr("property") == "og:image" }

        //returning the value of the 'content' property which contains the url of the image
        ogImageTag?.attr("content")
    }

    /**
     * Tries to download the favicon of a web page.If the favicon is not found or
     * glide throws an error , it will return null.
     * @param urlString the complete url of the web page
     */
    override suspend fun downloadFavicon(urlString: String): Drawable? =
        withContext(mDefaultDispatcher) {
            val url = URL(urlString)
            try {
                // TODO Catch clause does not execute
                Glide.with(mContext)
                    .load("${url.protocol}://${url.host}/favicon.ico")
                    .getDownloadedResource()
            } catch (exception: Exception) {
                null
            }
        }

}
