import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import LandingPage from './pages/LandingPage';
import BookingPage from './pages/BookingPage';
import './App.css';

function App() {
    return (
        <BrowserRouter>
            <div className="app">
                <Header />
                <Routes>
                    <Route path="/" element={<LandingPage />} />
                    <Route path="/event/:eventId" element={<BookingPage />} />
                </Routes>
                <footer className="footer">
                    <p>Spring Boot + React — Full-Stack Event Ticketing</p>
                </footer>
            </div>
        </BrowserRouter>
    );
}

export default App;
