# User API Spec

## Registrasi User

**Endpoint**: `POST /register`

**Request Body**:

```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "role": "string"
}
```

**Response**

```json
{
  "message": "berhasil mendaftar",
  "error": false,
  "data": [
    {
      "email": "example@gmail.com"
    }
  ]
}
```

## Login User

**Endpoint**: `POST /login`

**Request Body**:

```json
{
  "email": "string",
  "password": "string"
}
```

**Response**

```json
{
  "message": "berhasil login",
  "error": false,
  "data": [
    {
      "id": 123566152345,
      "email": "example@gmail.com",
      "role": "penyedia_jasa",
      "token": "tokeaskdnaksnd"
    }
  ]
}
```

## Get User / User By Id

**Endpoint**: `GET /users`
**Endpoint**: `GET /users/:id`

**Response (penyedia_jasa)**

```json
{
  "pesan": "berhasil mendapatkan user",
  "data": [
    {
      "id_user": "123",
      "email": "dummy@example.com",
      "name": "Dummy User",
      "username": "dummy1",
      "password": "HASH",
      "phone_number": "123456789",
      "photo_url": "https://example.com/dummy_user.jpg",
      "gender": "male",
      "address": "123 Dummy Street, Dummy City",
      "peran": "penyedia_jasa",
      "personal_data": {
        "skill": ["programming, design"],
        "deskripsi": "saya sangat senang mencuci pakaian"
      },
      "jasa_digunakan": [],
      "jasa_diberikan": [
        {
          "id_form_jasa": "456"
        },
        {
          "id_form_jasa": "789"
        }
      ],
      "feedback": [
        {
          "id_form_jasa": "456",
          "rating": 4,
          "masukan": "Great service, very satisfied!"
        },
        {
          "id_form_jasa": "789",
          "rating": 5,
          "masukan": "Excellent job, exceeded expectations!"
        }
      ]
    }
  ]
}
```

**Response (pengguna_jasa)**

```json
{
  "pesan": "berhasil mendapatkan user",
  "data": [
    {
      "id_user": "321",
      "email": "dummy2@example.com",
      "name": "Dummy User2",
      "username": "dummy2",
      "password": "HASH",
      "phone_number": "081231232",
      "photo_url": "https://example.com/dummy_user2.jpg",
      "gender": "male",
      "address": "123 Dummy Street2, Dummy City",
      "role": "pengguna jasa",
      "personal_data": {},
      "jasa_digunakan": [
        {
          "id_form_jasa": "456"
        },
        {
          "id_form_jasa": "789"
        }
      ],
      "jasa_diberikan": [],
      "feedback": []
    }
  ]
}
```

## update data user

**Endpoint**: `PUT /users/edit-profile/:id`

**Request Body**:

```json
{
  "email": "newemail@example.com",
  "name": "New Name",
  "password": "newpassword",
  "phone_number": "1234567890",
  "gender": "male",
  "address": "New Address",
  "personal_data": {
    "skill": ["New Skill", "Another Skill"],
    "deskripsi": "New description"
  }
}
```

**Response**

```json
{
  "Message": "Berhasil edit"
}
```

## Edit Photo Profile

**Endpoint**: `PUT /users/edit-photo/:id`

**Request Body (Form Data)**:

```json
{
  "photo": "images.png"
}
```

**Response**

```json
{
  "Message": "Berhasil uploud photo url: https://url.png"
}
```

## lowongan saya (proses dan selesai, pengguna jasa dan pemeberi jasa)

**Endpoint**: `GET /users/get-status-data`

**Request Body (Form Data)**:

```json
{
  "status": "proses",
  "pengguna_jasa": "zaid (username)"
}
```

**Response**

```json
{
  "message": "berhasil",
  "data": ["data pekerjaan proses", "data pekerjaan selesai"]
}
```

# Form Jasa

## Get Jasa dan Get jasa By Id

**Endpoint**: `GET /jasa`

**Endpoint**: `GET /jasa/:id`

**Response**

```json
{
  "pesan": "berhasil mendapatkan form jasa",
  "data": [
    {
      "id": "456",
      "job_name": "Web Development",
      "start_day": "2023-12-01",
      "end_day": "2023-12-15",
      "start_time": "09:00 AM",
      "end_time": "05:00 PM",
      "salary": 500000,
      "address": "456 Service Street, Service City",
      "description": "membuat halaman web static dengan html + css",
      "status": "prosess",
      "pengguna_jasa": "dummy2",
      "pemberi_jasa": "dummy",
      "feedback": {
        "rating": 5,
        "masukan": "Excellent job, exceeded expectations!"
      }
    }
  ]
}
```

## Buat Form Jasa

**Endpoint**: `POST /jasa`

**Request Body**:

```json
{
  "job_name": "Pembersihan kolam",
  "start_day": "2023-12-10",
  "end_day": "2023-12-12",
  "start_time": "08:00",
  "end_time": "17:00",
  "salary": 500000,
  "address": "Jl. Contoh No. 123",
  "description": "Dibutuhkan tenaga untuk membersihkan rumah",
  "status": "selesai",
  "pengguna_jasa": "zaid",
  "pemberi_jasa": "john_doe",
  "feedback": [
    {
      "rating": 5,
      "masukan": "Sangat baik pelayanannya"
    }
  ]
}
```

**Response**

```json
{
  "message": "berhasil"
}
```

## Edit Form Jasa

**Endpoint**: `PUT /jasa/:id`

**Request Body**:
```json
{
  "feedbacks": {
    "comment": "bagus",
    "rating": 5
  }
}
```
