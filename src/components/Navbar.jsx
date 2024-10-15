import React from 'react';
import { Link } from 'react-router-dom';

function Navbar() {
  return (
    <nav className="navbar">
      <div className="logo">ZOO-DON</div>
      <ul>
        <li><Link to="/">Home</Link></li>
        <li><Link to="/donation">Donate</Link></li>
        <li><Link to="/almanac">Almanac</Link></li>
      </ul>
    </nav>
  );
}

export default Navbar;
