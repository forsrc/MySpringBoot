package com.forsrc.boot.base.dao;

import java.io.Serializable;
import org.springframework.data.repository.CrudRepository;

public interface CrudDao<T extends Object, ID extends Serializable> //extends CrudRepository<T, ID>
{
}
