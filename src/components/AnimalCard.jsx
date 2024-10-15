import React from 'react';

function AnimalCard({ animal }) {
  return (
    <div className="animal-card">
      <h3>{animal.name}</h3>
      <h4><strong>Species:</strong> {animal.species}</h4>
      <h6>{animal.description}</h6>
    </div>
  );
}

export default AnimalCard;
