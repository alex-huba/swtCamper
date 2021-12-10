package swtcamper.backend.entities;

public enum UserRole {
  PROVIDER,
  // User hat (1) Provider Ansicht, (2) Renter Ansicht (beides in einem)
  RENTER,
  // Operator hat (1) Provider Ansicht, (2) Renter Ansicht, (3) Operator (Admin) Ansicht
  OPERATOR,
}
