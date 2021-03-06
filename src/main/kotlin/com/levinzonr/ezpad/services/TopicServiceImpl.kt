package com.levinzonr.ezpad.services

import com.google.gson.Gson
import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.Topic
import com.levinzonr.ezpad.domain.repositories.TopicRepository
import com.levinzonr.ezpad.utils.fromJsonFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TopicServiceImpl : TopicService {

    @Autowired
    private lateinit var repo: TopicRepository

    override fun createTopic(name: String) : Topic {
        return repo.save(Topic(name = name))
    }

    override fun getTopics(): List<Topic> {
        return repo.findAll().toList().sortedBy { it.name }
    }

    override fun findById(id: Long): Topic {
        return repo.findById(id).orElseThrow { NotFoundException.Builder(Topic::class).buildWithId(id.toString()) }
    }

    override fun findByIdOrNull(id: Long): Topic? {
        return repo.findById(id).orElse(null)
    }

    override fun editTopic(id: Long, name: String?): Topic {
        val topic = findById(id)
        val newTopic = topic.copy(
                name = name ?: topic.name
        )
        return repo.save(newTopic)
    }

    override fun deleteTopic(id: Long) {
        repo.delete(findById(id))
    }

    override fun init() {

    }
}