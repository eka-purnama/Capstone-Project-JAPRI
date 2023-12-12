# Menggunakan Node.js versi 14 sebagai base image
FROM node:18-alpine

# Membuat dan mengarahkan direktori kerja di dalam container
WORKDIR /app

# Menyalin package.json dan package-lock.json untuk instalasi dependensi
COPY package*.json ./

# Menyalin semua file dengan ekstensi .json ke dalam container
COPY *.json ./

# Menginstal dependensi
RUN npm install

# Menyalin seluruh kode sumber proyek ke dalam container
COPY . .

EXPOSE 8080

# Menjalankan aplikasi saat container dimulai
CMD ["node", "index.js"]