package com.backend.service.impl;

import com.backend.repository.PostsRepository;
import com.backend.repository.UsersRepository;
import com.backend.service.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsServiceImpl implements PostsService {
    public final PostsRepository postsRepository;
    public final UsersRepository usersRepository;
}
