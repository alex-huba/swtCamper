package swtcamper.backend.entities;

public enum UserRole {
  RENTER,
  // Provider can use renters + providers functionalities
  PROVIDER,
  // Operator can use renter + providers + operators functionalities
  OPERATOR,
}
