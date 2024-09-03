-- Apaga o banco de dados se já existir
DROP DATABASE IF EXISTS pombo_db;

-- Cria o banco de dados
CREATE DATABASE pombo_db;

-- Usa o banco de dados criado
USE pombo_db;

-- Criação da tabela `usuarios`
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    cpf CHAR(11) NOT NULL UNIQUE,
    foto VARCHAR(255),
    is_admin BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Criação da tabela `pruus`
CREATE TABLE pruus (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid CHAR(36) NOT NULL UNIQUE,
    texto VARCHAR(300) NOT NULL,
    data_hora_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantidade_likes INT DEFAULT 0,
    bloqueado BOOLEAN DEFAULT FALSE,
    imagem VARCHAR(255),
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Criação da tabela `likes`
CREATE TABLE likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    pruu_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_like_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_like_pruu FOREIGN KEY (pruu_id) REFERENCES pruus(id) ON DELETE CASCADE,
    UNIQUE (usuario_id, pruu_id)
);

-- Criação da tabela `pruu_reports`
CREATE TABLE pruu_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pruu_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    data_hora_report TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_report_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_report_pruu FOREIGN KEY (pruu_id) REFERENCES pruus(id) ON DELETE CASCADE
);

-- Verifica se todas as tabelas foram criadas corretamente
SHOW TABLES;

-- Exibe a estrutura das tabelas
DESCRIBE usuarios;
DESCRIBE pruus;
DESCRIBE likes;
DESCRIBE pruu_reports;

-- Comando para garantir que a configuração foi concluída corretamente
SELECT 'Database setup complete' AS status;
