package com.nott

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import com.flickr4java.flickr.photos.Photo
import com.flickr4java.flickr.photos.PhotoList
import com.flickr4java.flickr.photos.SearchParameters
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*

private lateinit var API_KEY: String
private lateinit var SECRET: String
private lateinit var OUTPUT_PATH: String

fun main(args: Array<String>) {
    val configPath = args[0]
    setupProperties(configPath)

    val searchQuery = args[1]
    val photos = collectImagesByQuery(searchQuery)
    photos.map {
        storePhoto(OUTPUT_PATH, it)
    }
}

private fun setupProperties(filePath: String) {
    val content = FileUtils.openInputStream(File(filePath))
    val properties = Properties().apply {
        load(content)
    }
    properties.let {
        API_KEY = it.getProperty("API_KEY")
        SECRET = it.getProperty("SECRET")
        OUTPUT_PATH = it.getProperty("OUTPUT_PATH")
    }
}

private fun collectImagesByQuery(query: String): PhotoList<Photo> {
    val flickr = Flickr(API_KEY, SECRET, REST())
    val sp = SearchParameters().apply {
        media = "photos"
        extras = setOf("url_sq", "tags")
        text = query
        tagMode = "all"
        sort = SearchParameters.RELEVANCE
    }
    return flickr.photosInterface.search(sp, 100, 1)
}

private fun storePhoto(outputPath: String, photo: Photo) {
    try {
        FileUtils.copyURLToFile(
            URL(photo.squareLargeUrl),
            File(outputPath + photo.id + ".jpg")
        )
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
}