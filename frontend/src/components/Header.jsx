import { Link, useLocation } from 'react-router-dom';
import './Header.css';

export default function Header() {
    const location = useLocation();
    const isHome = location.pathname === '/';

    return (
        <header className="site-header">
            <div className="header-content">
                <Link to="/" className="logo">
                    <span className="logo-icon">🎭</span>
                    <span className="logo-text">Grand Theater</span>
                </Link>

                <nav className="nav">
                    {!isHome && (
                        <Link to="/" className="nav-link">
                            ← Back to Events
                        </Link>
                    )}
                </nav>
            </div>
        </header>
    );
}
