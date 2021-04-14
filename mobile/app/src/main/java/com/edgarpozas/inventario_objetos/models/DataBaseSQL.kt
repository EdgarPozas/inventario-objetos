package com.edgarpozas.inventario_objetos.models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.edgarpozas.inventario_objetos.utils.DATABASE_NAME
import com.edgarpozas.inventario_objetos.utils.DATABASE_VERSION

class DataBaseSQL(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_USERS)
        db.execSQL(SQL_CREATE_ROOMS)
        db.execSQL(SQL_CREATE_POSITIONS)
        db.execSQL(SQL_CREATE_OBJECTS)
        db.execSQL(SQL_CREATE_OBJECTS_TAGS)
        db.execSQL(SQL_CREATE_OBJECTS_SHARED)
        db.execSQL(SQL_CREATE_OBJECTS_POSITIONS)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_USERS)
        db.execSQL(SQL_DELETE_ROOMS)
        db.execSQL(SQL_DELETE_POSITIONS)
        db.execSQL(SQL_DELETE_OBJECTS)
        db.execSQL(SQL_DELETE_OBJECTS_TAGS)
        db.execSQL(SQL_DELETE_OBJECTS_SHARED)
        db.execSQL(SQL_DELETE_OBJECTS_POSITIONS)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    private val SQL_CREATE_USERS="""
            create table users(
                _id text primary key,
                firstName text,
                lastName text,
                email text,
                password text,
                active integer,
                verified integer,
                createdAt text
            )""".trimIndent()
    private val SQL_CREATE_ROOMS="""create table rooms(
                _id text primary key,
                name text,
                description text,
                active integer,
                createdBy text,
                createdAt text
            )""".trimIndent()
    private val SQL_CREATE_POSITIONS="""create table positions(
                _id text primary key,
                latitude real,
                longitude real,
                altitude real,
                room text,
                createdBy text,
                createdAt text
            )""".trimIndent()
    private val SQL_CREATE_OBJECTS="""create table objects(
                _id text primary key,
                name text,
                description text,
                functionality text,
                urlImage text,
                urlSound text,
                price integer,
                active integer,
                createdBy text,
                createdAt text
            )""".trimIndent()
    private val SQL_CREATE_OBJECTS_TAGS="""create table objects_tags(
                _id text,
                tag text
            )""".trimIndent()
    private val SQL_CREATE_OBJECTS_SHARED="""create table objects_shared(
                _id text,
                _id_user text
            )""".trimIndent()
    private val SQL_CREATE_OBJECTS_POSITIONS="""create table objects_positions(
                _id text,
                _id_position text
            )""".trimIndent()

    private val SQL_DELETE_USERS="drop table if exists users"
    private val SQL_DELETE_ROOMS="drop table if exists rooms"
    private val SQL_DELETE_POSITIONS="drop table if exists positions"
    private val SQL_DELETE_OBJECTS="drop table if exists objects"
    private val SQL_DELETE_OBJECTS_TAGS="drop table if exists objects_tags"
    private val SQL_DELETE_OBJECTS_SHARED="drop table if exists objects_shared"
    private val SQL_DELETE_OBJECTS_POSITIONS="drop table if exists objects_positions"
}