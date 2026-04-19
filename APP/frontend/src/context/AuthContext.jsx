import React, { createContext, useState, useEffect } from 'react';
import api from '../api';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const savedUser = JSON.parse(localStorage.getItem('user'));
        if (savedUser) {
            setUser(savedUser);
        }
        setLoading(false);
    }, []);

    const login = async (username, password) => {
        const response = await api.post('/auth/signin', { username, password });
        if (response.data.accessToken) {
            localStorage.setItem('user', JSON.stringify(response.data));
            setUser(response.data);
        }
        return response.data;
    };

    const logout = () => {
        localStorage.removeItem('user');
        setUser(null);
    };

    const isAdmin = user?.roles.includes('ROLE_ADMIN');
    const isEditor = user?.roles.includes('ROLE_EDITOR') || isAdmin;

    return (
        <AuthContext.Provider value={{ user, login, logout, isAdmin, isEditor, loading }}>
            {children}
        </AuthContext.Provider>
    );
};
