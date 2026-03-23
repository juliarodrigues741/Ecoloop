CREATE DATABASE ecopontos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ecopontos;

CREATE TABLE usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  email VARCHAR(150) UNIQUE NOT NULL,
  senha_hash VARCHAR(255) NOT NULL,
  foto_perfil VARCHAR(255),
  nivel VARCHAR(50) DEFAULT 'Bronze',
  pontos INT DEFAULT 0,
  data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE configuracoes_usuario (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  idioma VARCHAR(20) DEFAULT 'pt-BR',
  tema_escuro BOOLEAN DEFAULT FALSE,
  notificacao_email BOOLEAN DEFAULT TRUE,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE materiais_enviados (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  descricao TEXT NOT NULL,
  tipo_arquivo VARCHAR(20) CHECK (tipo_arquivo IN ('foto', 'video')),
  caminho_arquivo VARCHAR(255) NOT NULL,
  data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  pontos_gerados INT DEFAULT 0,
  status VARCHAR(20) DEFAULT 'pendente' CHECK (status IN ('pendente', 'aprovado', 'recusado')),
  data_avaliacao TIMESTAMP NULL,
  comentario_avaliacao TEXT,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE beneficios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  descricao TEXT,
  categoria VARCHAR(50),
  pontos_necessarios INT NOT NULL,
  imagem_url VARCHAR(255)
);

CREATE TABLE resgates (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  beneficio_id INT NOT NULL,
  data_resgate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  pontos_gastos INT NOT NULL,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  FOREIGN KEY (beneficio_id) REFERENCES beneficios(id) ON DELETE CASCADE
);

CREATE TABLE conquistas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(100) NOT NULL,
  descricao TEXT,
  pontos_recompensa INT DEFAULT 0,
  nivel_requerido VARCHAR(50),
  imagem_url VARCHAR(255)
);

CREATE TABLE usuarios_conquistas (
  usuario_id INT NOT NULL,
  conquista_id INT NOT NULL,
  data_conquista TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (usuario_id, conquista_id),
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  FOREIGN KEY (conquista_id) REFERENCES conquistas(id) ON DELETE CASCADE
);

CREATE TABLE ranking_local (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  posicao INT,
  pontos INT,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE notificacoes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  mensagem TEXT NOT NULL,
  tipo VARCHAR(50),
  lida BOOLEAN DEFAULT FALSE,
  data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE logs_acoes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NULL,
  acao VARCHAR(100),
  descricao TEXT,
  data_acao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE SET NULL
);
