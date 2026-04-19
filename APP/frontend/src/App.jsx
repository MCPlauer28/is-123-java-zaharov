import React, { useContext, useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom';
import { AuthContext } from './context/AuthContext';
import api from './api';

// Компонент навигации
const Navbar = () => {
  const { user, logout, isAdmin, isEditor } = useContext(AuthContext);
  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
      <div className="container">
        <Link className="navbar-brand" to="/">My Blog</Link>
        <div className="navbar-nav ms-auto">
          <Link className="nav-link" to="/">Главная</Link>
          {user ? (
            <>
              {isEditor && <Link className="nav-link" to="/editor">Кабинет редактора</Link>}
              {isAdmin && <Link className="nav-link" to="/admin">Админка</Link>}
              <button className="btn btn-outline-light ms-2" onClick={logout}>Выйти ({user.username})</button>
            </>
          ) : (
            <Link className="nav-link" to="/login">Войти</Link>
          )}
        </div>
      </div>
    </nav>
  );
};

// Страница входа
const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useContext(AuthContext);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await login(username, password);
      window.location.href = '/';
    } catch (err) {
      setError('Неверный логин или пароль');
    }
  };

  return (
    <div className="container" style={{maxWidth: '400px'}}>
      <h2 className="mb-3">Вход</h2>
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Логин</label>
          <input className="form-control" value={username} onChange={e => setUsername(e.target.value)} required />
        </div>
        <div className="mb-3">
          <label className="form-label">Пароль</label>
          <input className="form-control" type="password" value={password} onChange={e => setPassword(e.target.value)} required />
        </div>
        <button className="btn btn-primary w-100" type="submit">Войти</button>
      </form>
      <p className="mt-3">Дефолтный админ: admin / admin123</p>
    </div>
  );
};

// Заглушка главной
const Home = () => (
  <div className="container">
    <h1>Добро пожаловать в блог!</h1>
    <p>Тут будут отображаться статьи из вашей базы Firebird.</p>
  </div>
);

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </Router>
  );
}

export default App;
