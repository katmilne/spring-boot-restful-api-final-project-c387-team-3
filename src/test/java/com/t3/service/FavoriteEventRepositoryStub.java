package com.t3.service;

import com.t3.models.Event;
import com.t3.models.User;
import com.t3.repository.FavoriteEventRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class FavoriteEventRepositoryStub implements FavoriteEventRepository {
    @Override
    public Set<Event> findByUser(User user) {
        return null;
    }

    @Override
    public Event findByIdAndUser(Long eventId, User user) {
        Event event = new Event();
        event.setId(15L);
        Set<Event> favEvents = new HashSet<>();
        favEvents.add(event);
        user.setFavoriteEvents(favEvents);
        if (event.getId()==eventId){
            return event;
        }
        return null;
    }

    @Override
    public List<Event> findAll() {
        return null;
    }

    @Override
    public List<Event> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Event> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Event entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Event> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Event> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Event> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Event> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Event> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Event> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Event> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Event getOne(Long aLong) {
        return null;
    }

    @Override
    public Event getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Event> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Event> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Event> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Event> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Event> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Event> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Event, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
