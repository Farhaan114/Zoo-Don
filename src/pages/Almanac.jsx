import React, { useEffect, useState } from 'react';
import axios from 'axios';
import AnimalCard from '../components/AnimalCard';

function Almanac() {
  const [animals, setAnimals] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Fetch data from API (replace with your actual API endpoint)
    axios.get('http://localhost:8088/data/endangered-species')
      .then(response => {
        setAnimals(response.data); // Assuming the API returns an array of animal objects
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching endangered species:', error);
        setError('Failed to fetch endangered species data.');
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <p>Loading...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  return (
    <div className='home-container'>
    <div className="almanac-container">
      <h1>Endangered Species Almanac - 2024</h1>
      <div className="card-grid">
        {animals.map((animal, index) => (
          <AnimalCard key={index} animal={animal} />
        ))}
      </div>
    </div>
    </div>
  );
}

export default Almanac;
